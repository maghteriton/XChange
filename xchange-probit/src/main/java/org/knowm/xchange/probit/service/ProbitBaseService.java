package org.knowm.xchange.probit.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.probit.ProbitDigest;
import org.knowm.xchange.probit.ProbitExchange;
import org.knowm.xchange.service.BaseResilientExchangeService;

import si.mazi.rescu.ParamsDigest;

public class ProbitBaseService extends BaseResilientExchangeService<Exchange> {

  protected final MarketAPI marketAPI;
  protected final AccountAPI accountAPI;
  protected final TradeAPI tradeAPI;
  protected final ParamsDigest signatureCreator;
  protected final String apiKey;

  public ProbitBaseService(ProbitExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
    marketAPI = service(exchange, MarketAPI.class);
    accountAPI = service(exchange, AccountAPI.class);
    tradeAPI = service(exchange, TradeAPI.class);

    String secretKey = exchange.getExchangeSpecification().getSecretKey();
    apiKey = exchange.getExchangeSpecification().getApiKey();
    signatureCreator = ProbitDigest.createInstance(apiKey, secretKey);
  }

  private <T> T service(ProbitExchange exchange, Class<T> clazz) {
    return ExchangeRestProxyBuilder.forInterface(clazz, exchange.getExchangeSpecification())
        .build();
  }
}
