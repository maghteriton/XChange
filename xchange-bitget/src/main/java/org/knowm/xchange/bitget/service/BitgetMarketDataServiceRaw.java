package org.knowm.xchange.bitget.service;

import org.knowm.xchange.bitget.BitgetBaseService;
import org.knowm.xchange.bitget.BitgetExchange;
import org.knowm.xchange.bitget.model.dto.response.BitgetCoinsResponse;
import org.knowm.xchange.bitget.model.dto.response.BitgetOrderBookResponse;
import org.knowm.xchange.bitget.model.dto.response.BitgetSymbolsResponse;
import org.knowm.xchange.client.ResilienceRegistries;

import java.io.IOException;
import java.util.List;

import static org.knowm.xchange.bitget.BitgetResilience.PUBLIC_REST_ENDPOINT_RATE_LIMITER;
import static org.knowm.xchange.bitget.service.exception.BitgetExceptionClassifier.classifyingExceptions;

public class BitgetMarketDataServiceRaw extends BitgetBaseService {

  private static final String DEFAULT_ORDERBOOK_LIMIT = "50";

  protected BitgetMarketDataServiceRaw(
      BitgetExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public List<BitgetSymbolsResponse> getBitgetSymbols(String symbol) throws IOException {
    return classifyingExceptions(
        () ->
            decorateApiCall(() -> marketAPI.getSymbols(symbol))
                .withRateLimiter(rateLimiter(PUBLIC_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }

  public List<BitgetCoinsResponse> getBitgetCoins(String coin) throws IOException {
    return classifyingExceptions(
        () ->
            decorateApiCall(() -> marketAPI.getCoins(coin))
                .withRateLimiter(rateLimiter(PUBLIC_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }

  public BitgetOrderBookResponse getBitgetOrderBook(String symbol) throws IOException {
    return classifyingExceptions(
        () ->
            decorateApiCall(() -> marketAPI.getOrderBook(symbol, DEFAULT_ORDERBOOK_LIMIT))
                .withRateLimiter(rateLimiter(PUBLIC_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }

  public List<String[]> getBitgetCandles(
      String symbol, String granularity, String startTime, String endTime) throws IOException {
    return classifyingExceptions(
        () ->
            decorateApiCall(() -> marketAPI.getCandles(symbol, granularity, startTime, endTime))
                .withRateLimiter(rateLimiter(PUBLIC_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }
}
