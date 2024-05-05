package org.knowm.xchange.coinw;

import java.io.IOException;
import java.util.*;
import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.coinw.service.account.CoinwAccountService;
import org.knowm.xchange.coinw.service.market.CoinwMarketDataService;
import org.knowm.xchange.coinw.service.trade.CoinwTradeService;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.exceptions.ExchangeException;

public class CoinwExchange extends BaseExchange implements Exchange {

  private static ResilienceRegistries RESILIENCE_REGISTRIES;

  @Override
  protected void initServices() {
    this.marketDataService = new CoinwMarketDataService(this, getResilienceRegistries());
    this.tradeService = new CoinwTradeService(this, getResilienceRegistries());
    this.accountService = new CoinwAccountService(this, getResilienceRegistries());
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass());
    exchangeSpecification.setSslUri("https://api.coinw.com");
    exchangeSpecification.setHost("https://api.coinw.com");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("Probit");
    exchangeSpecification.setExchangeDescription("Probit");
    exchangeSpecification.getResilience().setRateLimiterEnabled(true);
    return exchangeSpecification;
  }

  @Override
  public ResilienceRegistries getResilienceRegistries() {
    if (RESILIENCE_REGISTRIES == null) {
      RESILIENCE_REGISTRIES = CoinwResilience.createRegistries();
    }
    return RESILIENCE_REGISTRIES;
  }

  @Override
  public void applySpecification(ExchangeSpecification exchangeSpecification) {
    super.applySpecification(exchangeSpecification);
  }

  @Override
  public void remoteInit() throws IOException, ExchangeException {
    // single commission rate for all currencies

    accountService.getDepositAddress(new Currency("cake"), "bsc");

/*    this.exchangeMetaData =
        CoinwAdapter.adaptToExchangeMetaData(this.exchangeMetaData, marketData, currency);*/
  }
}
