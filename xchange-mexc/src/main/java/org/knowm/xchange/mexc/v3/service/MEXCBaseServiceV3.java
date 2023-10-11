package org.knowm.xchange.mexc.v3.service;

import java.util.concurrent.TimeUnit;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.mexc.v3.MEXCAuthenticatedV3;
import org.knowm.xchange.service.BaseService;
import org.knowm.xchange.utils.nonce.CurrentTimeIncrementalNonceFactory;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

public class MEXCBaseServiceV3 implements BaseService {

  protected final MEXCAuthenticatedV3 mexcAuthenticatedV3;
  protected final ParamsDigest signatureCreatorV3;
  protected final SynchronizedValueFactory<Long> nonceFactoryV3 = new CurrentTimeIncrementalNonceFactory(TimeUnit.MILLISECONDS);
  protected final String apiKeyV3;

  public MEXCBaseServiceV3(Exchange exchange) {
    mexcAuthenticatedV3 = ExchangeRestProxyBuilder.forInterface(MEXCAuthenticatedV3.class, exchange.getExchangeSpecification()).build();
    signatureCreatorV3 =
        MEXCDigestV3.createInstance(exchange.getExchangeSpecification().getSecretKey());
    apiKeyV3 = exchange.getExchangeSpecification().getApiKey();
  }
}
