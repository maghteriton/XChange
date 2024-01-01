package org.knowm.xchange.bingx.service;

import java.util.concurrent.TimeUnit;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.bingx.BingxDigest;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.service.BaseService;
import org.knowm.xchange.utils.nonce.CurrentTimeIncrementalNonceFactory;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

public class BingxBaseService implements BaseService {

  protected final MarketAPI marketAPI;
  protected final AccountAPI accountAPI;
  protected final TradeAPI tradeAPI;
  protected final ParamsDigest signatureCreator;
  protected final SynchronizedValueFactory<Long> nonceFactory =
      new CurrentTimeIncrementalNonceFactory(TimeUnit.MILLISECONDS);
  protected final String apiKey;

  public BingxBaseService(Exchange exchange) {
    marketAPI =
        ExchangeRestProxyBuilder.forInterface(MarketAPI.class, exchange.getExchangeSpecification())
            .build();

    accountAPI =
        ExchangeRestProxyBuilder.forInterface(AccountAPI.class, exchange.getExchangeSpecification())
            .build();

    tradeAPI =
            ExchangeRestProxyBuilder.forInterface(TradeAPI.class, exchange.getExchangeSpecification())
                    .build();

    signatureCreator =
        BingxDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());
    apiKey = exchange.getExchangeSpecification().getApiKey();
  }
}
