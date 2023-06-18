package org.knowm.xchange.mexc.service;

import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.mexc.MEXCAdapters;
import org.knowm.xchange.mexc.dto.market.MEXCCurrencyInfo;
import org.knowm.xchange.mexc.dto.market.MEXCDepth;
import org.knowm.xchange.mexc.dto.market.MEXCSymbols;
import org.knowm.xchange.service.marketdata.MarketDataService;

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

  public List<MEXCCurrencyInfo> getCoinList(@Nullable String currency) throws IOException {
    List<MEXCCurrencyInfo> currencyInfoList;
    try {
      currencyInfoList =
          mexcAuthenticated.getCoinList(currency != null ? currency.toUpperCase() : null).getData();
    } catch (MEXCException e) {
      throw new ExchangeException(e);
    }
    return currencyInfoList;
  }
}
