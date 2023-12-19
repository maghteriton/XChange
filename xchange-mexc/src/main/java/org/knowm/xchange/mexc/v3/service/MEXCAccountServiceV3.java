package org.knowm.xchange.mexc.v3.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.mexc.v3.MEXCAdaptersV3;
import org.knowm.xchange.mexc.v3.dto.CoinInfo;
import org.knowm.xchange.service.account.AccountService;

public class MEXCAccountServiceV3 extends MEXCAccountServiceV3Raw implements AccountService {

  private static final Map<Instrument, Boolean> defaultSymbolMap = new HashMap<>();

  public MEXCAccountServiceV3(Exchange exchange) {
    super(exchange);
  }

  public CoinInfo getCoin(Currency currency) throws IOException {
    List<CoinInfo> allCoins = getAllCoins();
    for (CoinInfo coinInfo : allCoins) {
      if (coinInfo.getCoin().equalsIgnoreCase(currency.getCurrencyCode())) {
        return coinInfo;
      }
    }

    return null;
  }

  public Map<Instrument, Boolean> getSymbols() throws IOException {
    if (defaultSymbolMap.isEmpty()) {
      List<String> defaultSymbolsList = getDefaultSymbols();
      for (String symbol : defaultSymbolsList) {
        Instrument instrument = MEXCAdaptersV3.adaptSymbol(symbol, Currency.USDT);
        if (instrument != null) {
          defaultSymbolMap.put(instrument, true);
        }
      }
    }

    return defaultSymbolMap;
  }
}
