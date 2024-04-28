package org.knowm.xchange.probit;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.probit.dto.response.ProbitBalanceDTO;
import org.knowm.xchange.probit.dto.response.ProbitCurrencyDTO;
import org.knowm.xchange.probit.dto.response.ProbitMarketDTO;
import org.knowm.xchange.probit.dto.response.ProbitResultDTO;
import org.knowm.xchange.probit.service.account.ProbitAccountService;
import org.knowm.xchange.probit.service.market.ProbitMarketDataService;
import org.knowm.xchange.probit.service.trade.ProbitTradeService;
import org.knowm.xchange.service.trade.params.DefaultCancelOrderByCurrencyPairAndIdParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsAll;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsSorted;
import org.knowm.xchange.service.trade.params.orders.DefaultQueryOrderParamCurrencyPair;

public class ProbitExchange extends BaseExchange implements Exchange {

  private static ResilienceRegistries RESILIENCE_REGISTRIES;

  @Override
  protected void initServices() {
    this.marketDataService = new ProbitMarketDataService(this, getResilienceRegistries());
    this.tradeService = new ProbitTradeService(this, getResilienceRegistries());
    this.accountService = new ProbitAccountService(this, getResilienceRegistries());
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass());
    exchangeSpecification.setSslUri("https://api.probit.com/api/exchange");
    exchangeSpecification.setHost("https://www.probit.com/");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("Probit");
    exchangeSpecification.setExchangeDescription("Probit");
    exchangeSpecification.getResilience().setRateLimiterEnabled(true);
    return exchangeSpecification;
  }

  @Override
  public ResilienceRegistries getResilienceRegistries() {
    if (RESILIENCE_REGISTRIES == null) {
      RESILIENCE_REGISTRIES = ProbitResilience.createRegistries();
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
    List<ProbitMarketDTO> marketData = ((ProbitMarketDataService) marketDataService).getMarket();
    List<ProbitCurrencyDTO> currency = ((ProbitMarketDataService) marketDataService).getCurrency();

    ((ProbitAccountService) accountService)
        .setSupportedInstruments(ProbitAdapter.adaptSupportedInstruments(marketData));
    this.exchangeMetaData =
        ProbitAdapter.adaptToExchangeMetaData(this.exchangeMetaData, marketData, currency);
  }
}
