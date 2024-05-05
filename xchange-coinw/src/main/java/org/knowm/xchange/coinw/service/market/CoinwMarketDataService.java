package org.knowm.xchange.coinw.service.market;

import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.coinw.CoinwExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.CandleStickData;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.params.CandleStickDataParams;
import org.knowm.xchange.service.trade.params.DefaultCandleStickParam;

import java.io.IOException;
import java.util.List;

public class CoinwMarketDataService extends CoinwMarketDataServiceRaw
    implements MarketDataService {

  public CoinwMarketDataService(
          CoinwExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }
}
