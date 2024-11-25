package org.knowm.xchange.coinex.service.trade;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.coinex.CoinexAdapters;
import org.knowm.xchange.coinex.dto.trade.CoinexCancelOrderRequest;
import org.knowm.xchange.coinex.dto.trade.CoinexOrderRequestPayload;
import org.knowm.xchange.coinex.dto.trade.CoinexOrderResponse;
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
import java.util.Map;


public class CoinexTradeService extends CoinexTradeServiceRaw implements TradeService {

    public CoinexTradeService(Exchange exchange) {
        super(exchange);
    }

    @Override
    public Class[] getRequiredCancelOrderParamClasses() {
        return new Class[]{CancelOrderByIdParams.class, CancelOrderByCurrencyPair.class};
    }

    @Override
    public Class getRequiredOrderQueryParamClass() {
        return DefaultQueryOrderParamCurrencyPair.class;
    }

    @Override
    public String placeLimitOrder(LimitOrder limitOrder) throws IOException {
        CoinexOrderRequestPayload orderRequestPayload = CoinexAdapters.adaptLimitOrder(limitOrder);
        return String.valueOf(placeOrder(orderRequestPayload).getOrderId());
    }

    @Override
    public boolean cancelOrder(CancelOrderParams orderParams) throws IOException {
        if (orderParams instanceof CancelOrderByIdParams
                && orderParams instanceof CancelOrderByCurrencyPair) {
            String orderId = ((CancelOrderByIdParams) orderParams).getOrderId();
            CurrencyPair currencyPair = ((CancelOrderByCurrencyPair) orderParams).getCurrencyPair();
            CoinexCancelOrderRequest cancelOrderRequest = CoinexAdapters.adaptCancelOrderParams(currencyPair, orderId);
            return cancelSingleOrder(cancelOrderRequest).getOrderId() != null;
        }

        throw new NotAvailableFromExchangeException("cancelOrder in Coinex needs orderId and currency.");
    }

    @Override
    public Collection<Order> getOrder(OrderQueryParams... orderQueryParams) throws IOException {
        List<Order> orders = new ArrayList<>();
        for (OrderQueryParams param : orderQueryParams) {
            if (!(param instanceof DefaultQueryOrderParamCurrencyPair)) {
                throw new NotAvailableFromExchangeException(
                        "getOrder in Coinex needs orderId and currency.");
            }

            DefaultQueryOrderParamCurrencyPair orderParam = (DefaultQueryOrderParamCurrencyPair) param;
            CoinexOrderResponse orderStatus = getOrderStatus(
                    orderParam.getCurrencyPair(),
                    orderParam.getOrderId());
            Order order = CoinexAdapters.adaptOrder(orderParam.getCurrencyPair(), orderParam.getOrderId(), orderStatus);
            orders.add(order);
        }
        return orders;
    }

}
