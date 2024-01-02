package org.knowm.xchange.bingx.service.market;

import java.io.IOException;
import org.knowm.xchange.bingx.BingxAdapter;
import org.knowm.xchange.bingx.BingxExchange;
import org.knowm.xchange.bingx.model.KLineInterval;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.CandleStickData;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.params.CandleStickDataParams;
import org.knowm.xchange.service.trade.params.DefaultCandleStickParamWithLimit;

public class BingxMarketDataService extends BingxMarketDataServiceRaw implements MarketDataService {
  public BingxMarketDataService(BingxExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  @Override
  public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args) throws IOException {
    return BingxAdapter.adaptOrderBook(
        currencyPair, getMarketDepth(BingxAdapter.adaptToBingxSymbol(currencyPair)));
  }

  @Override
  public CandleStickData getCandleStickData(CurrencyPair currencyPair, CandleStickDataParams params)
      throws IOException {

    if (!(params instanceof DefaultCandleStickParamWithLimit)) {
      throw new NotYetImplementedForExchangeException(
          "Only DefaultCandleStickParamWithLimit is supported");
    }

    DefaultCandleStickParamWithLimit defaultCandleStickParam =
        (DefaultCandleStickParamWithLimit) params;
    long periodInSecs = defaultCandleStickParam.getPeriodInSecs();
    KLineInterval interval = KLineInterval.min30;
    for (KLineInterval bingxInterval : KLineInterval.values()) {
      if (bingxInterval.getSeconds() == periodInSecs) {
        interval = bingxInterval;
        break;
      }
    }

    return BingxAdapter.adaptCandleStickData(
        currencyPair,
        getKLineData(
            currencyPair.getBase().getCurrencyCode(),
            interval,
            defaultCandleStickParam.getLimit(),
            defaultCandleStickParam.getStartDate(),
            defaultCandleStickParam.getEndDate()));
  }
}
