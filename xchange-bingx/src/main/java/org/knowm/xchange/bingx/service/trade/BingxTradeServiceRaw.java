package org.knowm.xchange.bingx.service.trade;

import static org.knowm.xchange.bingx.BingxExceptionClassifier.classifyingExceptions;
import static org.knowm.xchange.bingx.BingxResilience.PRIVATE_REST_ENDPOINT_RATE_LIMITER;

import java.io.IOException;
import java.math.BigDecimal;
import org.knowm.xchange.bingx.BingxExchange;
import org.knowm.xchange.bingx.dto.BingxOrderDTO;
import org.knowm.xchange.bingx.dto.TradeCommissionRateDTO;
import org.knowm.xchange.bingx.dto.wrapper.BingxCancelLimitOrderWrapper;
import org.knowm.xchange.bingx.dto.wrapper.BingxCreateLimitOrderWrapper;
import org.knowm.xchange.bingx.model.BingxOrderType;
import org.knowm.xchange.bingx.service.BingxBaseService;
import org.knowm.xchange.client.ResilienceRegistries;

public class BingxTradeServiceRaw extends BingxBaseService {
  public BingxTradeServiceRaw(BingxExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public TradeCommissionRateDTO getCommissionRate(String symbol) throws IOException {
    return classifyingExceptions(
        () ->
            decorateApiCall(
                    () ->
                        tradeAPI.getCommissionRate(
                            apiKey, nonceFactory, 5000, signatureCreator, symbol))
                .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }

  public BingxOrderDTO queryOrder(String symbol, String orderId) throws IOException {

    return classifyingExceptions(
        () ->
            decorateApiCall(
                    () ->
                        tradeAPI.queryOrder(
                            apiKey, nonceFactory, 5000, signatureCreator, symbol, orderId))
                .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }

  public BingxCreateLimitOrderWrapper createLimitOrder(
      String symbol, String side, BigDecimal quantity, BigDecimal price) throws IOException {

    return classifyingExceptions(
        () ->
            decorateApiCall(
                    () ->
                        tradeAPI.createLimitOrder(
                            apiKey,
                            nonceFactory,
                            5000,
                            signatureCreator,
                            symbol,
                            side,
                            BingxOrderType.LIMIT.value(),
                            quantity,
                            price))
                .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }

  public BingxCancelLimitOrderWrapper cancelLimitOrder(String symbol, String orderId)
      throws IOException {

    return classifyingExceptions(
        () ->
            decorateApiCall(
                    () ->
                        tradeAPI.cancelLimitOrder(
                            apiKey, nonceFactory, 5000, signatureCreator, symbol, orderId))
                .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }
}
