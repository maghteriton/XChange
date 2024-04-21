package org.knowm.xchange.huobi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.huobi.dto.account.HuobiTransactFeeRate;
import org.knowm.xchange.huobi.dto.marketdata.HuobiAsset;
import org.knowm.xchange.huobi.dto.marketdata.HuobiAssetPair;
import org.knowm.xchange.huobi.dto.marketdata.HuobiCurrencyWrapper;
import org.knowm.xchange.huobi.service.*;

public class HuobiExchange extends BaseExchange implements Exchange {

  @Override
  protected void initServices() {
    this.marketDataService = new HuobiMarketDataService(this);
    this.tradeService = new HuobiTradeService(this);
    this.accountService = new HuobiAccountService(this);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass());
    exchangeSpecification.setSslUri("https://api.huobi.pro");
    exchangeSpecification.setHost("api.huobi.pro");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("Huobi");
    exchangeSpecification.setExchangeDescription(
        "Huobi is a Chinese digital currency trading platform and exchange based in Beijing");
    return exchangeSpecification;
  }

  @Override
  public void remoteInit() throws IOException, ExchangeException {
    HuobiAssetPair[] assetPairs =
        ((HuobiMarketDataServiceRaw) marketDataService).getHuobiAssetPairs();
    HuobiAsset[] assets = ((HuobiMarketDataServiceRaw) marketDataService).getHuobiAssets();
    HuobiCurrencyWrapper[] huobiCurrencies =
        ((HuobiAccountServiceRaw) accountService).getHuobiCurrencies("");
    //List<HuobiTransactFeeRate> huobiTransactFeeRateList = new ArrayList<>();
/*    List<HuobiAssetPair> usdtAssetPairs =
        Arrays.stream(assetPairs)
            .filter(huobiAssetPair -> huobiAssetPair.getSymbol().contains("usdt"))
            .collect(Collectors.toList());

    for (HuobiAssetPair assetPair : usdtAssetPairs) {
      try {
        HuobiTransactFeeRate[] transactFeeRate =
            ((HuobiAccountServiceRaw) accountService).getTransactFeeRate(assetPair.getSymbol());
        if (transactFeeRate.length != 0) {
          huobiTransactFeeRateList.add(transactFeeRate[0]);
        }
      } catch (Exception e) {
        // do nothing, its invalid symbol returned by Huobi
      }
    }*/

    exchangeMetaData =
        HuobiAdapters.adaptToExchangeMetaData(
            assetPairs, assets, exchangeMetaData, huobiCurrencies, new ArrayList<>());
  }
}
