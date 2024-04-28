package org.knowm.xchange.probit.service.market;

import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.probit.ProbitAdapter;
import org.knowm.xchange.probit.ProbitExchange;
import org.knowm.xchange.probit.dto.response.*;
import org.knowm.xchange.probit.service.ProbitBaseService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.knowm.xchange.probit.ProbitResilience.GROUP_3_ENDPOINT_RATE_LIMITER;

public class ProbitMarketDataServiceRaw extends ProbitBaseService {
  private static final String DEFAULT_ORDER = "desc";
  private static final String DEFAULT_LIMIT = "2000";

  public ProbitMarketDataServiceRaw(
      ProbitExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public ProbitResultDTO<List<ProbitMarketDTO>> getMarketData() throws IOException {
    return decorateApiCall(marketAPI::getMarket)
        .withRetry(retry("getMarket"))
        .withRateLimiter(rateLimiter(GROUP_3_ENDPOINT_RATE_LIMITER))
        .call();
  }

  public ProbitResultDTO<List<ProbitCurrencyDTO>> getCurrencyData() throws IOException {
    return decorateApiCall(marketAPI::getCurrency)
        .withRetry(retry("getCurrency"))
        .withRateLimiter(rateLimiter(GROUP_3_ENDPOINT_RATE_LIMITER))
        .call();
  }

  public ProbitResultDTO<List<ProbitOrderBookDTO>> getOrderBookData(CurrencyPair currencyPair)
      throws IOException {
    return decorateApiCall(
            () -> marketAPI.getOrderBook(ProbitAdapter.adaptCurrencyPair(currencyPair)))
        .withRetry(retry("getOrderBook"))
        .withRateLimiter(rateLimiter(GROUP_3_ENDPOINT_RATE_LIMITER))
        .call();
  }

  public ProbitResultDTO<List<ProbitCandleDTO>> getCandleData(
      CurrencyPair currencyPair, Date startTime, Date endTime, String interval) throws IOException {
    return decorateApiCall(
            () ->
                marketAPI.getCandle(
                    ProbitAdapter.adaptCurrencyPair(currencyPair),
                    ProbitAdapter.formatDate(startTime),
                    ProbitAdapter.formatDate(endTime),
                    interval,
                    DEFAULT_ORDER,
                    DEFAULT_LIMIT))
        .withRetry(retry("getOrderBook"))
        .withRateLimiter(rateLimiter(GROUP_3_ENDPOINT_RATE_LIMITER))
        .call();
  }
}
