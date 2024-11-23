package org.knowm.xchange.coinex;

import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.coinex.dto.account.CoinexAssets;
import org.knowm.xchange.coinex.dto.market.CoinexMarketInfo;
import org.knowm.xchange.coinex.service.account.CoinexAccountService;
import org.knowm.xchange.coinex.service.account.CoinexAccountServiceRaw;
import org.knowm.xchange.coinex.service.market.CoinexMarketDataService;
import org.knowm.xchange.coinex.service.market.CoinexMarketServiceRaw;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.exceptions.ExchangeException;

import java.io.IOException;
import java.util.List;

public class CoinexExchange extends BaseExchange implements Exchange {

  @Override
  protected void initServices() {
    this.accountService = new CoinexAccountService(this);
    this.marketDataService = new CoinexMarketDataService(this);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass());
    exchangeSpecification.setSslUri("https://api.coinex.com");
    exchangeSpecification.setHost("www.coinex.com");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("Coinex");
    exchangeSpecification.setExchangeDescription("Coinex is a crypto-to-crypto exchange.");
    return exchangeSpecification;
  }

  @Override
  public void remoteInit() throws IOException, ExchangeException {
    List<CoinexMarketInfo> coinexMarketInfoList = ((CoinexMarketServiceRaw) marketDataService).getSymbols(null);
    List<CoinexAssets> coinexAssetList = ((CoinexAccountServiceRaw) accountService).getAssetList(null);
    exchangeMetaData = CoinexAdapters.adaptToExchangeMetaData(coinexMarketInfoList, coinexAssetList);

    accountService.getCurrencyChainStatus(new Currency("CET"), "CSC");
  }
}
