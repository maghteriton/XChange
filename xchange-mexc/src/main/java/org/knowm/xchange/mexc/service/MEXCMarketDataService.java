package org.knowm.xchange.mexc.service;

import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.CandleStickData;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.meta.CurrencyChainStatus;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.mexc.MEXCAdapters;
import org.knowm.xchange.mexc.dto.market.*;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.params.CandleStickDataParams;
import org.knowm.xchange.service.trade.params.DefaultCandleStickParam;

public class MEXCMarketDataService extends MEXCMarketDataServiceRaw implements MarketDataService {

  public MEXCMarketDataService(Exchange exchange) {
    super(exchange);
  }

  @Override
  public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args) throws IOException {

    String depth = "100";
    if (args != null && args.length == 1) {
      Object arg0 = args[0];
      if (!(arg0 instanceof String)) {
        throw new ExchangeException("Argument 0 must be an String!");
      } else {
        depth = (String) arg0;
      }
    }

    MEXCDepth mexcDepth;
    try {
      mexcDepth = getMEXCDepth(currencyPair, depth);
    } catch (MEXCException e) {
      throw new ExchangeException(e);
    }

    return MEXCAdapters.adaptDepth(currencyPair, mexcDepth);
  }

  public List<MEXCSymbols> getSymbols() throws IOException {
    List<MEXCSymbols> mexcSymbols;
    try {
      mexcSymbols = mexcAuthenticated.getSymbols().getData();
    } catch (MEXCException e) {
      throw new ExchangeException(e);
    }
    return mexcSymbols;
  }

  @Override
  public CandleStickData getCandleStickData(CurrencyPair currencyPair, CandleStickDataParams params)
      throws IOException {

    if (!(params instanceof DefaultCandleStickParam)) {
      throw new NotYetImplementedForExchangeException("Only DefaultCandleStickParam is supported");
    }

    DefaultCandleStickParam defaultCandleStickParam = (DefaultCandleStickParam) params;
    long after = defaultCandleStickParam.getEndDate().getTime() / 1000;
    long periodInSecs = defaultCandleStickParam.getPeriodInSecs();
    MEXCKlineInterval interval = MEXCKlineInterval.m30;
    for (MEXCKlineInterval mexcKlineInterval : MEXCKlineInterval.values()) {
      if (mexcKlineInterval.getSeconds() == periodInSecs) {
        interval = mexcKlineInterval;
        break;
      }
    }

    List<MEXCCandleData> mexcCandleStickDataList;
    try {
      mexcCandleStickDataList =
          getMEXCCandleStickData(currencyPair, interval.getCode(), after, 1000);
    } catch (MEXCException e) {
      throw new ExchangeException(e);
    }

    return MEXCAdapters.adaptCandleStickData(mexcCandleStickDataList, currencyPair);
  }
}
