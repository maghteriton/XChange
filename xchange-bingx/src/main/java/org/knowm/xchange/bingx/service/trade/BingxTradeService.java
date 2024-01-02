package org.knowm.xchange.bingx.service.trade;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.knowm.xchange.bingx.BingxAdapter;
import org.knowm.xchange.bingx.BingxExchange;
import org.knowm.xchange.bingx.dto.BingxOrderDTO;
import org.knowm.xchange.bingx.dto.wrapper.BingxCreateLimitOrderWrapper;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.CancelOrderByCurrencyPair;
import org.knowm.xchange.service.trade.params.CancelOrderByIdParams;
import org.knowm.xchange.service.trade.params.CancelOrderParams;
import org.knowm.xchange.service.trade.params.orders.DefaultQueryOrderParamCurrencyPair;
import org.knowm.xchange.service.trade.params.orders.OrderQueryParams;

public class BingxTradeService extends BingxTradeServiceRaw implements TradeService {
  public BingxTradeService(BingxExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  @Override
  public Class getRequiredOrderQueryParamClass() {
    return DefaultQueryOrderParamCurrencyPair.class;
  }

  public Class[] getRequiredCancelOrderParamClasses() {
    return new Class[] {CancelOrderByIdParams.class, CancelOrderByCurrencyPair.class};
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
      String symbol = BingxAdapter.adaptToBingxSymbol(queryOrderParamCurrencyPair.getCurrencyPair());

      BingxOrderDTO bingxOrderDTO = queryOrder(symbol, orderId);
      Order order = BingxAdapter.adaptOrder(bingxOrderDTO);
    }

    return orders;
  }

  @Override
  public boolean cancelOrder(CancelOrderParams orderParams) throws IOException {
    if (orderParams instanceof CancelOrderByIdParams
        && orderParams instanceof CancelOrderByCurrencyPair) {
      String orderId = ((CancelOrderByIdParams) orderParams).getOrderId();
      CurrencyPair currencyPair = ((CancelOrderByCurrencyPair) orderParams).getCurrencyPair();
      String symbol = BingxAdapter.adaptToBingxSymbol(currencyPair);
      return cancelLimitOrder(symbol, orderId).getOrderId() != null;
    } else {
      return false;
    }
  }

  @Override
  public String placeLimitOrder(LimitOrder limitOrder) throws IOException {
    BigDecimal limitPrice = limitOrder.getLimitPrice();
    BigDecimal originalAmount = limitOrder.getOriginalAmount();
    String symbol = BingxAdapter.adaptToBingxSymbol((CurrencyPair) limitOrder.getInstrument());
    String side = BingxAdapter.adaptToBingxSide(limitOrder.getType());

    BingxCreateLimitOrderWrapper bingxCreateLimitOrderWrapper =
        createLimitOrder(symbol, side, originalAmount, limitPrice);
    return String.valueOf(bingxCreateLimitOrderWrapper.getOrderId());
  }
}
