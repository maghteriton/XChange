package org.knowm.xchange.bingx.service.market;

import static org.knowm.xchange.bingx.BingxExceptionClassifier.classifyingExceptions;
import static org.knowm.xchange.bingx.BingxResilience.PRIVATE_REST_ENDPOINT_RATE_LIMITER;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.knowm.xchange.bingx.BingxExchange;
import org.knowm.xchange.bingx.dto.BingxMarketDepthDTO;
import org.knowm.xchange.bingx.dto.wrapper.BingxSymbolWrapper;
import org.knowm.xchange.bingx.model.KLineInterval;
import org.knowm.xchange.bingx.service.BingxBaseService;
import org.knowm.xchange.client.ResilienceRegistries;

public class BingxMarketDataServiceRaw extends BingxBaseService {
  public BingxMarketDataServiceRaw(
      BingxExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public BingxSymbolWrapper getSymbols() throws IOException {

    return classifyingExceptions(
        () ->
            decorateApiCall(marketAPI::getSymbols)
                .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }

  public BingxMarketDepthDTO getMarketDepth(String symbol) throws IOException {

    return classifyingExceptions(
        () ->
            decorateApiCall(() -> marketAPI.getMarketDepth(symbol, nonceFactory))
                .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }

  public List<List<String>> getKLineData(
      String symbol, KLineInterval interval, Integer limit, Date startTime, Date endTime)
      throws IOException {
    Long startTimeAsMs = startTime != null ? startTime.getTime() : null;
    Long endTimeAsMs = endTime != null ? endTime.getTime() : null;
    return classifyingExceptions(
        () ->
            decorateApiCall(
                    () ->
                        marketAPI.getKLineData(
                            symbol,
                            interval.code(),
                            limit,
                            endTimeAsMs,
                            startTimeAsMs,
                            nonceFactory))
                .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }
}
