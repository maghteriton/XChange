package org.knowm.xchange.bitget;

import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.bitget.model.BitgetAPIConstants;

import org.knowm.xchange.bitget.model.dto.response.BitgetCoinsResponse;
import org.knowm.xchange.bitget.model.dto.response.BitgetSymbolsResponse;
import org.knowm.xchange.bitget.service.BitgetAccountService;
import org.knowm.xchange.bitget.service.BitgetMarketDataService;
import org.knowm.xchange.bitget.service.BitgetTradeService;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.marketdata.CandleStickData;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.meta.CurrencyChainStatus;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.service.trade.params.*;

import java.io.IOException;
import java.util.*;

import org.joda.time.DateTime;
import org.knowm.xchange.service.trade.params.orders.DefaultQueryOrderParamCurrencyPair;
import org.knowm.xchange.service.trade.params.orders.OrderQueryParams;

public class BitgetExchange extends BaseExchange implements Exchange {

  private static ResilienceRegistries RESILIENCE_REGISTRIES;

  @Override
  protected void initServices() {
    this.marketDataService = new BitgetMarketDataService(this, getResilienceRegistries());
    this.accountService = new BitgetAccountService(this, getResilienceRegistries());
    this.tradeService = new BitgetTradeService(this, getResilienceRegistries());
  }

  @Override
  public ResilienceRegistries getResilienceRegistries() {
    if (RESILIENCE_REGISTRIES == null) {
      RESILIENCE_REGISTRIES = BitgetResilience.createRegistries();
    }
    return RESILIENCE_REGISTRIES;
  }

  @Override
  public void applySpecification(ExchangeSpecification exchangeSpecification) {
    super.applySpecification(exchangeSpecification);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass());
    exchangeSpecification.setSslUri(BitgetAPIConstants.BASE_URL);
    exchangeSpecification.getResilience().setRateLimiterEnabled(true);
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("Bitget");
    exchangeSpecification.setExchangeDescription("Bitget is a bitcoin and altcoin exchange.");
    return exchangeSpecification;
  }

  @Override
  public void remoteInit() throws IOException, ExchangeException {
    List<BitgetCoinsResponse> bitgetCoins = getMarketDataService().getBitgetCoins(null);
    List<BitgetSymbolsResponse> bitgetSymbols = getMarketDataService().getBitgetSymbols(null);
    ((BitgetAccountService) accountService).setBitgetCoinInformation(bitgetCoins);
    this.exchangeMetaData = BitgetAdapter.adaptMetadata(bitgetCoins, bitgetSymbols);
  }

  @Override
  public BitgetMarketDataService getMarketDataService() {
    return (BitgetMarketDataService) super.getMarketDataService();
  }

  @Override
  public BitgetTradeService getTradeService() {
    return (BitgetTradeService) super.getTradeService();
  }

  @Override
  public BitgetAccountService getAccountService() {
    return (BitgetAccountService) super.getAccountService();
  }
}
