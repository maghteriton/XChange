package org.knowm.xchange.bitget;

import com.google.common.base.Strings;
import org.knowm.xchange.bitget.apis.AccountAPI;
import org.knowm.xchange.bitget.apis.MarketAPI;
import org.knowm.xchange.bitget.apis.TradeAPI;
import org.knowm.xchange.bitget.service.exception.BitgetApiException;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.service.BaseResilientExchangeService;
import si.mazi.rescu.SynchronizedValueFactory;

public class BitgetBaseService extends BaseResilientExchangeService<BitgetExchange> {
  protected final AccountAPI accountAPI;
  protected final MarketAPI marketAPI;
  protected final TradeAPI tradeAPI;

  protected BitgetDigest digest;
  protected String apiKey;
  protected String passphrase;
  protected SynchronizedValueFactory<Long> nonceFactory;

  protected BitgetBaseService(BitgetExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
    // apis
    this.accountAPI = service(exchange, AccountAPI.class);
    this.marketAPI = service(exchange, MarketAPI.class);
    this.tradeAPI = service(exchange, TradeAPI.class);

    // authorization
    this.digest = BitgetDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());
    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    this.passphrase =
        (String)
            exchange.getExchangeSpecification().getExchangeSpecificParametersItem("passphrase");
    this.nonceFactory = exchange.getNonceFactory();
  }

  private <T> T service(BitgetExchange exchange, Class<T> clazz) {
    return ExchangeRestProxyBuilder.forInterface(clazz, exchange.getExchangeSpecification())
        .build();
  }

  protected void checkAuthenticated() {
    if (Strings.isNullOrEmpty(this.apiKey)) throw new BitgetApiException("Missing API key");
    if (this.digest == null) throw new BitgetApiException("Missing secret key");
    if (Strings.isNullOrEmpty(this.passphrase)) throw new BitgetApiException("Missing passphrase");
  }
}
