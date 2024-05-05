package org.knowm.xchange.coinw.service.trade;

import java.io.IOException;
import java.util.List;

import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.coinw.CoinwExchange;
import org.knowm.xchange.coinw.service.CoinwBaseService;

public class CoinwTradeServiceRaw extends CoinwBaseService {

  public CoinwTradeServiceRaw(CoinwExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

}
