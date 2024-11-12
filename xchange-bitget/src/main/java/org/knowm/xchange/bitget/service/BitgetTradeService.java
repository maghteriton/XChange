package org.knowm.xchange.bitget.service;

import org.knowm.xchange.bitget.BitgetAdapter;
import org.knowm.xchange.bitget.BitgetExchange;
import org.knowm.xchange.bitget.model.dto.response.BitgetOrderHistoryResponse;
import org.knowm.xchange.bitget.service.exception.BitgetApiException;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.CancelOrderByCurrencyPair;
import org.knowm.xchange.service.trade.params.CancelOrderByIdParams;
import org.knowm.xchange.service.trade.params.CancelOrderParams;
import org.knowm.xchange.service.trade.params.orders.DefaultQueryOrderParamCurrencyPair;
import org.knowm.xchange.service.trade.params.orders.OrderQueryParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BitgetTradeService extends BitgetTradeServiceRaw implements TradeService {
  public BitgetTradeService(
      BitgetExchange bitgetExchange, ResilienceRegistries resilienceRegistries) {
    super(bitgetExchange, resilienceRegistries);
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
  public String placeLimitOrder(LimitOrder limitOrder) throws IOException {
    try {
      return placeLimitOrder(
              BitgetAdapter.convertToSymbol(limitOrder.getInstrument()),
              BitgetAdapter.convertToSide(limitOrder.getType()),
              limitOrder.getLimitPrice(),
              limitOrder.getOriginalAmount())
          .getOrderId();
    } catch (BitgetApiException e) {
      throw new ExchangeException(e);
    }
  }

  @Override
  public boolean cancelOrder(CancelOrderParams orderParams) throws IOException {
    if (orderParams instanceof CancelOrderByIdParams
        && orderParams instanceof CancelOrderByCurrencyPair) {

      String orderId = ((CancelOrderByIdParams) orderParams).getOrderId();
      String symbol =
          BitgetAdapter.convertToSymbol(
              ((CancelOrderByCurrencyPair) orderParams).getCurrencyPair());
      return orderId.equals(cancelLimitOrder(symbol, orderId).getOrderId());
    }
    return false;
  }

  @Override
  public Collection<Order> getOrder(OrderQueryParams... orderQueryParams) throws IOException {
    List<Order> orders = new ArrayList<>();
    for (OrderQueryParams param : orderQueryParams) {
      if (!(param instanceof DefaultQueryOrderParamCurrencyPair)) {
        throw new NotAvailableFromExchangeException(
            "getOrder in Bitget needs orderId and currency pair");
      }
      DefaultQueryOrderParamCurrencyPair queryOrderParamCurrencyPair =
          (DefaultQueryOrderParamCurrencyPair) param;
      String orderId = queryOrderParamCurrencyPair.getOrderId();
      CurrencyPair currencyPair = queryOrderParamCurrencyPair.getCurrencyPair();
      String symbol = BitgetAdapter.convertToSymbol(currencyPair);
      List<BitgetOrderHistoryResponse> orderInfo = getOrderHistory(symbol, orderId);
      if(orderInfo.isEmpty()) {
        orderInfo.addAll(getOrderInfo(orderId));
      }
      orders.addAll(BitgetAdapter.adaptOrder(currencyPair, orderInfo));
    }
    return orders;
  }
}
