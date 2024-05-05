package org.knowm.xchange.coinw.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.coinw.CoinwDigest;
import org.knowm.xchange.coinw.CoinwExchange;
import org.knowm.xchange.service.BaseResilientExchangeService;

import si.mazi.rescu.ParamsDigest;

import java.time.Instant;

public class CoinwBaseService extends BaseResilientExchangeService<Exchange> {

  protected final MarketAPI marketAPI;
  protected final AccountAPI accountAPI;
  protected final TradeAPI tradeAPI;
  protected final ParamsDigest signatureCreator;
  protected final String apiKey;

  public CoinwBaseService(CoinwExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
    marketAPI = service(exchange, MarketAPI.class);
    accountAPI = service(exchange, AccountAPI.class);
    tradeAPI = service(exchange, TradeAPI.class);

    String secretKey = exchange.getExchangeSpecification().getSecretKey();
    apiKey = exchange.getExchangeSpecification().getApiKey();
    signatureCreator = CoinwDigest.createInstance(apiKey, secretKey);
  }

  private <T> T service(CoinwExchange exchange, Class<T> clazz) {
    return ExchangeRestProxyBuilder.forInterface(clazz, exchange.getExchangeSpecification())
        .build();
  }

  protected static String timestamp() {
    return Long.toString(Instant.now().toEpochMilli());
  }
}
