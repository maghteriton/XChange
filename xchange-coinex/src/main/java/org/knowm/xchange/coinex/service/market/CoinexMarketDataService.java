package org.knowm.xchange.coinex.service.market;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.service.marketdata.MarketDataService;


public class CoinexMarketDataService extends CoinexMarketServiceRaw implements MarketDataService {
    public CoinexMarketDataService(Exchange exchange) {
        super(exchange);
    }

}
