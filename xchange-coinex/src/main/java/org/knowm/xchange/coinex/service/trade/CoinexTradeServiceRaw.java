package org.knowm.xchange.coinex.service.trade;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.coinex.CoinexAdapters;
import org.knowm.xchange.coinex.dto.trade.CoinexCancelOrderRequest;
import org.knowm.xchange.coinex.dto.trade.CoinexOrderRequestPayload;
import org.knowm.xchange.coinex.dto.trade.CoinexOrderResponse;
import org.knowm.xchange.coinex.service.CoinexBaseService;
import org.knowm.xchange.currency.CurrencyPair;

import java.io.IOException;

public class CoinexTradeServiceRaw extends CoinexBaseService {
    protected CoinexTradeServiceRaw(Exchange exchange) {
        super(exchange);
    }

    public CoinexOrderResponse placeOrder(
            CoinexOrderRequestPayload orderRequestPayload)
            throws IOException {
        return checkResult(coinex.placeOrder(
                apiKey,
                signatureCreator,
                nonceFactory, orderRequestPayload));
    }

    public CoinexOrderResponse cancelSingleOrder(
            CoinexCancelOrderRequest cancelOrderRequest)
            throws IOException {
        return checkResult(coinex.cancelOrder(
                apiKey,
                signatureCreator,
                nonceFactory, cancelOrderRequest));
    }

    public CoinexOrderResponse getOrderStatus(
            CurrencyPair currencyPair, String orderId)
            throws IOException {
        return checkResult(coinex.getOrderStatus(
                apiKey,
                signatureCreator,
                nonceFactory, CoinexAdapters.getMarket(currencyPair), Long.valueOf(orderId)));
    }
}
