package org.knowm.xchange.coinw.service.market;

import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.coinw.CoinwExchange;
import org.knowm.xchange.coinw.service.CoinwBaseService;
import org.knowm.xchange.currency.CurrencyPair;


import java.io.IOException;
import java.util.Date;
import java.util.List;


public class CoinwMarketDataServiceRaw extends CoinwBaseService {
  private static final String DEFAULT_ORDER = "desc";
  private static final String DEFAULT_LIMIT = "2000";

  public CoinwMarketDataServiceRaw(
          CoinwExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }
}
