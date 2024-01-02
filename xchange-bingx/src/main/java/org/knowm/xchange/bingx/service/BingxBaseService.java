package org.knowm.xchange.bingx.service;

import java.util.concurrent.TimeUnit;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.bingx.BingxDigest;
import org.knowm.xchange.bingx.BingxExchange;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.service.BaseResilientExchangeService;
import org.knowm.xchange.utils.nonce.CurrentTimeIncrementalNonceFactory;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

public class BingxBaseService extends BaseResilientExchangeService<Exchange> {

  protected final MarketAPI marketAPI;
  protected final AccountAPI accountAPI;
  protected final TradeAPI tradeAPI;
  protected final ParamsDigest signatureCreator;
  protected final SynchronizedValueFactory<Long> nonceFactory =
      new CurrentTimeIncrementalNonceFactory(TimeUnit.MILLISECONDS);
  protected final String apiKey;

  public BingxBaseService(BingxExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
    marketAPI = service(exchange, MarketAPI.class);
    accountAPI = service(exchange, AccountAPI.class);
    tradeAPI = service(exchange, TradeAPI.class);

    signatureCreator =
        BingxDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());
    apiKey = exchange.getExchangeSpecification().getApiKey();
  }

  private <T> T service(BingxExchange exchange, Class<T> clazz) {
    return ExchangeRestProxyBuilder.forInterface(clazz, exchange.getExchangeSpecification())
        .build();
  }
}
