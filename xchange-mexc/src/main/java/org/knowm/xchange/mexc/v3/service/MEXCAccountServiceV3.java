package org.knowm.xchange.mexc.v3.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.mexc.v3.dto.CoinInfo;
import org.knowm.xchange.service.account.AccountService;

import java.io.IOException;
import java.util.List;

public class MEXCAccountServiceV3 extends MEXCAccountServiceV3Raw implements AccountService {

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
}
