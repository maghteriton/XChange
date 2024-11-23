package org.knowm.xchange.coinex.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.coinex.CoinexAuthenticated;
import org.knowm.xchange.service.BaseService;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

public class CoinexBaseService implements BaseService {

    protected final String apiKey;
    protected final CoinexAuthenticated coinex;
    protected final ParamsDigest signatureCreator;
    protected SynchronizedValueFactory<Long> nonceFactory;

    /**
     * Constructor for CoinexBaseService.
     *
     * @param exchange the exchange to be used for creating the service
     */
    protected CoinexBaseService(Exchange exchange) {
        this.nonceFactory = exchange.getNonceFactory();
        this.coinex =
                ExchangeRestProxyBuilder.forInterface(
                                CoinexAuthenticated.class, exchange.getExchangeSpecification())
                        .build();
        this.apiKey = exchange.getExchangeSpecification().getApiKey();
        this.signatureCreator =
                CoinexDigest.createInstance(apiKey, exchange.getExchangeSpecification().getSecretKey());
    }
}
