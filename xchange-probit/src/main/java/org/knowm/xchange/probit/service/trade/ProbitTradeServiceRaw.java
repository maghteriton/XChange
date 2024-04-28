package org.knowm.xchange.probit.service.trade;

import java.io.IOException;
import java.util.List;

import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.probit.ProbitAdapter;
import org.knowm.xchange.probit.ProbitExchange;
import org.knowm.xchange.probit.dto.request.ProbitCancelOrderRequestDTO;
import org.knowm.xchange.probit.dto.response.ProbitLimitOrderDTO;
import org.knowm.xchange.probit.dto.request.ProbitNewOrderRequestDTO;
import org.knowm.xchange.probit.dto.response.ProbitResultDTO;
import org.knowm.xchange.probit.service.ProbitBaseService;

import static org.knowm.xchange.probit.ProbitResilience.*;

public class ProbitTradeServiceRaw extends ProbitBaseService {

  public ProbitTradeServiceRaw(ProbitExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public ProbitResultDTO<ProbitLimitOrderDTO> createLimitOrder(
      ProbitNewOrderRequestDTO probitNewOrderRequestDTO) throws IOException {
    return decorateApiCall(() -> tradeAPI.newOrder(signatureCreator, probitNewOrderRequestDTO))
        .withRetry(retry("createLimitOrder"))
        .withRateLimiter(rateLimiter(GROUP_1_ENDPOINT_RATE_LIMITER))
        .call();
  }

  public ProbitResultDTO<ProbitLimitOrderDTO> cancelLimitOrder(
      ProbitCancelOrderRequestDTO probitCancelOrderRequestDTO) throws IOException {
    return decorateApiCall(
            () -> tradeAPI.cancelOrder(signatureCreator, probitCancelOrderRequestDTO))
        .withRetry(retry("createLimitOrder"))
        .withRateLimiter(rateLimiter(GROUP_2_ENDPOINT_RATE_LIMITER))
        .call();
  }

  public ProbitResultDTO<List<ProbitLimitOrderDTO>> getLimitOrder(
      String currencyPair, String orderId) throws IOException {
    return decorateApiCall(
            () ->
                tradeAPI.getOrder(
                    signatureCreator, currencyPair, orderId))
        .withRetry(retry("createLimitOrder"))
        .withRateLimiter(rateLimiter(GROUP_3_ENDPOINT_RATE_LIMITER))
        .call();
  }
}
