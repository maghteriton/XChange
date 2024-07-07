package org.knowm.xchange.bitget.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.knowm.xchange.bitget.BitgetBaseService;
import org.knowm.xchange.bitget.BitgetExchange;
import org.knowm.xchange.bitget.model.dto.response.BitgetCoinsResponse;
import org.knowm.xchange.bitget.model.dto.response.BitgetOrderBookResponse;
import org.knowm.xchange.bitget.model.dto.response.BitgetSymbolsResponse;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.exceptions.ExchangeException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.knowm.xchange.bitget.BitgetResilience.PUBLIC_REST_ENDPOINT_RATE_LIMITER;
import static org.knowm.xchange.bitget.service.exception.BitgetExceptionClassifier.classifyingExceptions;

public class BitgetMarketDataServiceRaw extends BitgetBaseService {

  private static final String DEFAULT_ORDER_BOOK_LIMIT = "50";
  private static final String DEFAULT_CACHE_KEY = "defaultKey";
  private final Cache<String, List<BitgetCoinsResponse>> coinsCache;

  protected BitgetMarketDataServiceRaw(
      BitgetExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);

    this.coinsCache = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.MINUTES).build();
  }

  public List<BitgetSymbolsResponse> getBitgetSymbols(String symbol) throws IOException {
    return classifyingExceptions(
        () ->
            decorateApiCall(() -> marketAPI.getSymbols(symbol))
                .withRateLimiter(rateLimiter(PUBLIC_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }

  public List<BitgetCoinsResponse> getBitgetCoins(String coin) {
    String cacheKey = coin == null ? DEFAULT_CACHE_KEY : coin;
    try {
      return coinsCache.get(
          cacheKey,
          () ->
              classifyingExceptions(
                  () ->
                      decorateApiCall(() -> marketAPI.getCoins(coin))
                          .withRateLimiter(rateLimiter(PUBLIC_REST_ENDPOINT_RATE_LIMITER))
                          .call()));
    } catch (ExecutionException e) {
      throw new ExchangeException(e);
    }
  }

  public BitgetOrderBookResponse getBitgetOrderBook(String symbol) throws IOException {
    return classifyingExceptions(
        () ->
            decorateApiCall(() -> marketAPI.getOrderBook(symbol, DEFAULT_ORDER_BOOK_LIMIT))
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
