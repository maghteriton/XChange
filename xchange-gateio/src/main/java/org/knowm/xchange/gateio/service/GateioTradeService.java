package org.knowm.xchange.gateio.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.gateio.GateioAdapters;
import org.knowm.xchange.gateio.dto.trade.GateioOpenOrders;
import org.knowm.xchange.gateio.dto.trade.GateioOrderStatus;
import org.knowm.xchange.gateio.dto.trade.GateioTrade;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.*;
import org.knowm.xchange.service.trade.params.orders.DefaultQueryOrderParamCurrencyPair;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;
import org.knowm.xchange.service.trade.params.orders.OrderQueryParams;

public class GateioTradeService extends GateioTradeServiceRaw implements TradeService {

  /**
   * Constructor
   *
   * @param exchange exchange
   */
  public GateioTradeService(Exchange exchange) {

    super(exchange);
  }

  @Override
  public OpenOrders getOpenOrders() throws IOException {
    return getOpenOrders(createOpenOrdersParams());
  }

  @Override
  public OpenOrders getOpenOrders(OpenOrdersParams params) throws IOException {
    GateioOpenOrders openOrders = super.getAllGateioOpenOrders();
    return GateioAdapters.adaptOpenOrders(openOrders);
  }

  @Override
  public String placeMarketOrder(MarketOrder marketOrder) throws IOException {

    throw new NotAvailableFromExchangeException();
  }

  /**
   * Submits a Limit Order to be executed on the Gateio Exchange for the desired market defined by
   * {@code CurrencyPair}. WARNING - Gateio will return true regardless of whether or not an order
   * actually gets created. The reason for this is that orders are simply submitted to a queue in
   * their back-end. One example for why an order might not get created is because there are
   * insufficient funds. The best attempt you can make to confirm that the order was created is to
   * poll {@link #getOpenOrders}. However, if the order is created and executed before it is caught
   * in its open state from calling {@link #getOpenOrders} then the only way to confirm would be
   * confirm the expected difference in funds available for your account.
   *
   * @return String "true"/"false" Used to determine if the order request was submitted
   *     successfully.
   */
  @Override
  public String placeLimitOrder(LimitOrder limitOrder) throws IOException {

    return String.valueOf(super.placeGateioLimitOrder(limitOrder));
  }

  @Override
  public boolean cancelOrder(String orderId) throws IOException {
    throw new NotAvailableFromExchangeException();
  }

  @Override
  public Class[] getRequiredCancelOrderParamClasses() {
    return new Class[] {CancelOrderByIdParams.class, CancelOrderByCurrencyPair.class};
  }

  @Override
  public boolean cancelOrder(CancelOrderParams orderParams) throws IOException {
    if (orderParams instanceof CancelOrderByIdParams
        && orderParams instanceof CancelOrderByCurrencyPair) {
      return cancelOrder(
          ((CancelOrderByIdParams) orderParams).getOrderId(),
          ((CancelOrderByCurrencyPair) orderParams).getCurrencyPair());
    } else {
      return false;
    }
  }

  /** Required parameter: {@link TradeHistoryParamCurrencyPair} */
  @Override
  public UserTrades getTradeHistory(TradeHistoryParams params)
      throws ExchangeException, IOException {

    CurrencyPair pair = ((TradeHistoryParamCurrencyPair) params).getCurrencyPair();
    List<GateioTrade> userTrades = getGateioTradeHistory(pair).getTrades();

    return GateioAdapters.adaptUserTrades(userTrades);
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
            "getOrder in gateio needs orderId and currency pair");
      }

      DefaultQueryOrderParamCurrencyPair queryOrderParamCurrencyPair =
          (DefaultQueryOrderParamCurrencyPair) param;
      String orderId = queryOrderParamCurrencyPair.getOrderId();
      CurrencyPair currencyPair = queryOrderParamCurrencyPair.getCurrencyPair();

      GateioOpenOrders gateioOpenOrders = super.getGateioOpenOrders(currencyPair);

      // first check open orders.
      if (!gateioOpenOrders.getOrders().isEmpty()) {
        OpenOrders openOrders = GateioAdapters.adaptOpenOrders(gateioOpenOrders);
        List<LimitOrder> limitOrderList = openOrders.getOpenOrders();
        orders.addAll(limitOrderList);
      } else {
        GateioOrderStatus gateioOrderStatus = getGateioOrderStatus(orderId, currencyPair);
        BigDecimal remainingAmount =
            gateioOrderStatus.getInitialAmount().subtract(gateioOrderStatus.getAmount());
        LimitOrder limitOrder =
            new LimitOrder.Builder(
                    GateioAdapters.adaptOrderType(gateioOrderStatus.getType()),
                    gateioOrderStatus.getCurrencyPair())
                .originalAmount(gateioOrderStatus.getInitialAmount())
                .remainingAmount(remainingAmount)
                .id(gateioOrderStatus.getOrderNumber())
                .limitPrice(gateioOrderStatus.getInitialRate())
                .orderStatus(GateioAdapters.adaptOrderStatus(gateioOrderStatus.getStatus()))
                .build();

        limitOrder.setAveragePrice(gateioOrderStatus.getRate());
        orders.add(limitOrder);
      }
    }

    return orders;
  }

  @Override
  public TradeHistoryParamCurrencyPair createTradeHistoryParams() {

    return new DefaultTradeHistoryParamCurrencyPair();
  }

  @Override
  public OpenOrdersParams createOpenOrdersParams() {
    return null;
  }
}
