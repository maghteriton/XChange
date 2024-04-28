package org.knowm.xchange.probit.service.trade;

import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.probit.ProbitAdapter;
import org.knowm.xchange.probit.ProbitException;
import org.knowm.xchange.probit.ProbitExchange;
import org.knowm.xchange.probit.dto.request.ProbitCancelOrderRequestDTO;
import org.knowm.xchange.probit.dto.response.ProbitLimitOrderDTO;
import org.knowm.xchange.probit.dto.request.ProbitNewOrderRequestDTO;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.CancelOrderByCurrencyPair;
import org.knowm.xchange.service.trade.params.CancelOrderByIdParams;
import org.knowm.xchange.service.trade.params.CancelOrderParams;
import org.knowm.xchange.service.trade.params.orders.DefaultQueryOrderParamCurrencyPair;
import org.knowm.xchange.service.trade.params.orders.OrderQueryParams;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProbitTradeService extends ProbitTradeServiceRaw implements TradeService {

  private static final String PROBIT_LIMIT_ORDER_TYPE = "limit";
  private static final String GOOD_TILL_CANCELLED = "gtc";

  public ProbitTradeService(ProbitExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  @Override
  public Class[] getRequiredCancelOrderParamClasses() {
    return new Class[] {CancelOrderByIdParams.class, CancelOrderByCurrencyPair.class};
  }

  @Override
  public Class getRequiredOrderQueryParamClass() {
    return DefaultQueryOrderParamCurrencyPair.class;
  }

  @Override
  public Collection<Order> getOrder(OrderQueryParams... orderQueryParams) throws IOException {
    List<Order> orders = new ArrayList<>();
    for (OrderQueryParams param : orderQueryParams) {
      if (!(param instanceof DefaultQueryOrderParamCurrencyPair)) {
        throw new NotAvailableFromExchangeException(
            "getOrder in bingx needs orderId and currency pair");
      }
      DefaultQueryOrderParamCurrencyPair queryOrderParamCurrencyPair =
          (DefaultQueryOrderParamCurrencyPair) param;
      String orderId = queryOrderParamCurrencyPair.getOrderId();
      String currencyPair =
          ProbitAdapter.adaptCurrencyPair(queryOrderParamCurrencyPair.getCurrencyPair());

      InstrumentMetaData instrumentMetaData =
          this.exchange
              .getExchangeMetaData()
              .getInstruments()
              .get(queryOrderParamCurrencyPair.getCurrencyPair());
      orders.add(
          ProbitAdapter.adaptOrder(
              getLimitOrder(currencyPair, orderId).getData().get(0),
              instrumentMetaData.getTradingFee()));
    }

    return orders;
  }

  @Override
  public boolean cancelOrder(CancelOrderParams orderParams) throws IOException {
    if (orderParams instanceof CancelOrderByIdParams
        && orderParams instanceof CancelOrderByCurrencyPair) {
      String orderId = ((CancelOrderByIdParams) orderParams).getOrderId();
      CurrencyPair currencyPair = ((CancelOrderByCurrencyPair) orderParams).getCurrencyPair();

      ProbitCancelOrderRequestDTO probitCancelOrderRequestDTO =
          ProbitCancelOrderRequestDTO.builder()
              .order_id(orderId)
              .market_id(ProbitAdapter.adaptCurrencyPair(currencyPair))
              .build();

      return cancelLimitOrder(probitCancelOrderRequestDTO).getData().getId() != null;
    } else {
      return false;
    }
  }

  @Override
  public String placeLimitOrder(LimitOrder limitOrder) throws IOException {
    BigDecimal limitPrice = limitOrder.getLimitPrice();
    BigDecimal originalAmount = limitOrder.getOriginalAmount();
    CurrencyPair currencyPair = (CurrencyPair) limitOrder.getInstrument();
    String side = ProbitAdapter.adaptToProbitSide(limitOrder.getType());

    String orderId;

    ProbitNewOrderRequestDTO requestDTO =
        ProbitNewOrderRequestDTO.builder()
            .market_id(ProbitAdapter.adaptCurrencyPair(currencyPair))
            .time_in_force(GOOD_TILL_CANCELLED)
            .side(side)
            .type(PROBIT_LIMIT_ORDER_TYPE)
            .limit_price(limitPrice.toPlainString())
            .quantity(originalAmount.toPlainString())
            .build();

    try {
      ProbitLimitOrderDTO newOrderDTOS = createLimitOrder(requestDTO).getData();
      orderId = newOrderDTOS.getId();
    } catch (ProbitException e) {
      throw new ExchangeException(
          String.format(
              "Code: %s, Message: %s, Details: %s",
              e.getErrorCode(), e.getMessage(), e.getDetails()));
    }

    return orderId;
  }
}
