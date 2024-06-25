package org.knowm.xchange.bitget.service;

import org.knowm.xchange.bitget.BitgetBaseService;
import org.knowm.xchange.bitget.BitgetExchange;
import org.knowm.xchange.bitget.model.dto.request.BitgetCancelLimitOrderRequest;
import org.knowm.xchange.bitget.model.dto.request.BitgetLimitOrderRequest;
import org.knowm.xchange.bitget.model.dto.response.BitgetOrderHistoryResponse;
import org.knowm.xchange.bitget.model.dto.response.BitgetOrderIdResponse;
import org.knowm.xchange.client.ResilienceRegistries;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.knowm.xchange.bitget.BitgetResilience.PRIVATE_REST_ENDPOINT_RATE_LIMITER;
import static org.knowm.xchange.bitget.service.exception.BitgetExceptionClassifier.classifyingExceptions;

public class BitgetTradeServiceRaw extends BitgetBaseService {
  protected BitgetTradeServiceRaw(
      BitgetExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public BitgetOrderIdResponse placeLimitOrder(
      String symbol, String side, BigDecimal price, BigDecimal size) throws IOException {
    checkAuthenticated();
    BitgetLimitOrderRequest limitOrderRequest =
        BitgetLimitOrderRequest.builder()
            .symbol(symbol)
            .side(side)
            .orderType("limit")
            .force("gtc")
            .price(price.toPlainString())
            .size(size.toPlainString())
            .build();

    return classifyingExceptions(
        () ->
            decorateApiCall(
                    () ->
                        tradeAPI.placeLimitOrder(
                            apiKey, digest, passphrase, nonceFactory, limitOrderRequest))
                .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }

  public BitgetOrderIdResponse cancelLimitOrder(String symbol, String orderId) throws IOException {
    checkAuthenticated();
    BitgetCancelLimitOrderRequest cancelLimitOrderRequest =
        BitgetCancelLimitOrderRequest.builder().symbol(symbol).orderId(orderId).build();

    return classifyingExceptions(
        () ->
            decorateApiCall(
                    () ->
                        tradeAPI.cancelLimitOrder(
                            apiKey, digest, passphrase, nonceFactory, cancelLimitOrderRequest))
                .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }

  public List<BitgetOrderHistoryResponse> getOrderHistory(String symbol, String orderId)
      throws IOException {
    checkAuthenticated();
    return classifyingExceptions(
        () ->
            decorateApiCall(
                    () ->
                        tradeAPI.getOrderHistory(
                            apiKey, digest, passphrase, nonceFactory, symbol, orderId))
                .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }
}
