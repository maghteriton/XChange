package org.knowm.xchange.mexc;

import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.mexc.dto.market.MEXCCurrencyInfo;
import org.knowm.xchange.mexc.dto.market.MEXCSymbols;
import org.knowm.xchange.mexc.service.MEXCAccountService;
import org.knowm.xchange.mexc.service.MEXCAccountServiceRaw;
import org.knowm.xchange.mexc.service.MEXCMarketDataService;
import org.knowm.xchange.mexc.service.MEXCTradeService;
import org.knowm.xchange.mexc.v3.MEXCExchangeV3;

import java.io.IOException;
import java.util.List;

public class MEXCExchange extends BaseExchange implements Exchange {

  @Override
  protected void initServices() {
    this.marketDataService = new MEXCMarketDataService(this);
    this.tradeService = new MEXCTradeService(this);
    this.accountService = new MEXCAccountService(this);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass());
    exchangeSpecification.setSslUri("https://www.mexc.com");
    exchangeSpecification.setHost("mexc.com");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("MEXC");
    exchangeSpecification.setExchangeDescription("MEXC");
    return exchangeSpecification;
  }

  @Override
  public void applySpecification(ExchangeSpecification exchangeSpecification) {
    super.applySpecification(exchangeSpecification);
    ExchangeSpecification exSpecMexcV3 = new MEXCExchangeV3().getDefaultExchangeSpecification();
    exSpecMexcV3.setApiKey(exchangeSpecification.getApiKey());
    exSpecMexcV3.setSecretKey(exchangeSpecification.getSecretKey());
    Exchange mexcExchangeV3 = ExchangeFactory.INSTANCE.createExchange(exSpecMexcV3);
    this.accountService = new MEXCAccountService(this, mexcExchangeV3);
  }

  @Override
  public void remoteInit() throws IOException, ExchangeException {
    List<MEXCSymbols> symbols = ((MEXCMarketDataService) marketDataService).getSymbols();
    List<MEXCCurrencyInfo> currencyList = ((MEXCAccountServiceRaw) accountService).getCurrencyList(null);
    exchangeMetaData = MEXCAdapters.adaptToExchangeMetaData(symbols, currencyList);
  }
}
