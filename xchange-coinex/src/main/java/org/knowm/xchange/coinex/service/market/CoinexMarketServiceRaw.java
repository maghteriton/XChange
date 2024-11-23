package org.knowm.xchange.coinex.service.market;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.coinex.dto.market.CoinexMarketInfo;
import org.knowm.xchange.coinex.service.CoinexBaseService;
import org.knowm.xchange.exceptions.ExchangeException;

import javax.annotation.Nullable;
import java.util.List;

public class CoinexMarketServiceRaw extends CoinexBaseService {

    public CoinexMarketServiceRaw(Exchange exchange) {
        super(exchange);
    }

    public List<CoinexMarketInfo> getSymbols(@Nullable String market) {
        List<CoinexMarketInfo> coinexSymbols;
        try {
            coinexSymbols = coinex.getMarket(market).getData();
        } catch (Exception e) {
            throw new ExchangeException(e.getMessage(), e);
        }
        return coinexSymbols;
    }


}
