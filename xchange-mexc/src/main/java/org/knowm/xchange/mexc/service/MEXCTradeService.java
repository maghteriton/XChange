package org.knowm.xchange.mexc.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.mexc.MEXCAdapters;
import org.knowm.xchange.mexc.dto.MEXCResult;
import org.knowm.xchange.mexc.dto.trade.MEXCDeal;
import org.knowm.xchange.mexc.dto.trade.MEXCOrder;
import org.knowm.xchange.mexc.dto.trade.MEXCOrderRequestPayload;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.CancelOrderByIdParams;
import org.knowm.xchange.service.trade.params.CancelOrderParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamCurrencyPair;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.orders.DefaultQueryOrderParam;
import org.knowm.xchange.service.trade.params.orders.OrderQueryParams;

public class MEXCTradeService extends MEXCTradeServiceRaw implements TradeService {

  public MEXCTradeService(Exchange exchange) {
    super(exchange);
  }

  @Override
  public String placeLimitOrder(LimitOrder limitOrder) throws IOException {
    try {
      MEXCOrderRequestPayload orderRequestPayload = MEXCAdapters.adaptOrder(limitOrder);
      return placeOrder(orderRequestPayload).getData();
    } catch (MEXCException e) {
      throw new ExchangeException(e);
    }
  }

  @Override
  public Collection<Order> getOrder(String... orderIds) throws IOException {
    try {
      MEXCResult<List<MEXCOrder>> ordersResult = getOrders(Arrays.asList(orderIds));
      return ordersResult.getData().stream()
          .map(MEXCAdapters::adaptOrder)
          .collect(Collectors.toList());
    } catch (MEXCException e) {
      throw new ExchangeException(e);
    }
  }

  @Override
  public Class getRequiredOrderQueryParamClass() {
    return DefaultQueryOrderParam.class;
  }

  @Override
  public Collection<Order> getOrder(OrderQueryParams... orderQueryParams) throws IOException {
    List<Order> orders = new ArrayList<>();
    for (OrderQueryParams param : orderQueryParams) {
      if (!(param instanceof DefaultQueryOrderParam)) {
        throw new NotAvailableFromExchangeException("getOrder in MEXC needs orderId.");
      }
      DefaultQueryOrderParam defaultQueryOrderParam = (DefaultQueryOrderParam) param;
      MEXCResult<List<MEXCOrder>> ordersResult;

      try {
        ordersResult = getOrders(Collections.singletonList(defaultQueryOrderParam.getOrderId()));
      } catch (MEXCException e) {
        throw new ExchangeException(e);
      }

      List<MEXCOrder> mexcOrderList = ordersResult.getData();
      if (!mexcOrderList.isEmpty()) {
        MEXCOrder mexcOrder = mexcOrderList.get(0);
        LimitOrder limitOrder =
            new LimitOrder(
                MEXCAdapters.adaptOrderType(mexcOrder.getType()),
                new BigDecimal(mexcOrder.getQuantity())
                    .multiply(new BigDecimal(mexcOrder.getPrice())),
                MEXCAdapters.adaptSymbol(mexcOrder.getSymbol()),
                mexcOrder.getId(),
                new Date(mexcOrder.getCreateTime()),
                new BigDecimal(mexcOrder.getPrice()),
                new BigDecimal(mexcOrder.getPrice()),
                new BigDecimal(mexcOrder.getDealAmount()),
                null,
                Order.OrderStatus.valueOf(mexcOrder.getState().toUpperCase(Locale.ENGLISH)),
                null);

        orders.add(limitOrder);
      }
    }

    return orders;
  }

  @Override
  public UserTrades getTradeHistory(TradeHistoryParams params)
      throws ExchangeException, IOException {
    CurrencyPair currencyPair = ((TradeHistoryParamCurrencyPair) params).getCurrencyPair();

    MEXCResult<List<MEXCDeal>> tradeHistory;
    try {
      tradeHistory = getTradeHistory(currencyPair);
    } catch (MEXCException e) {
      throw new ExchangeException(e);
    }

    return MEXCAdapters.adaptUserTrades(tradeHistory.getData());
  }

  @Override
  public boolean cancelOrder(CancelOrderParams orderParams) throws IOException {
    if (!(orderParams instanceof CancelOrderByIdParams)) {
      throw new NotAvailableFromExchangeException("cancelOrder in MEXC needs orderId.");
    }
    String orderId = ((CancelOrderByIdParams) orderParams).getOrderId();
    MEXCResult<Map<String, String>> cancelOrderMap;
    try {
      cancelOrderMap = cancelOrderById(orderId);
    } catch (MEXCException e) {
      throw new ExchangeException(e);
    }

    return MEXCAdapters.adaptCancelOrder(cancelOrderMap.getData(), orderId);
  }
}
