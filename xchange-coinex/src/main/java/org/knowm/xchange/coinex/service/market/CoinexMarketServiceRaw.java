package org.knowm.xchange.coinex.service.market;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.coinex.dto.market.CoinexKlineData;
import org.knowm.xchange.coinex.dto.market.CoinexMarketDepth;
import org.knowm.xchange.coinex.dto.market.CoinexMarketInfo;
import org.knowm.xchange.coinex.service.CoinexBaseService;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

public class CoinexMarketServiceRaw extends CoinexBaseService {

    public CoinexMarketServiceRaw(Exchange exchange) {
        super(exchange);
    }

    public List<CoinexMarketInfo> getSymbols(@Nullable String market) throws IOException {
        return checkResult(coinex.getMarket(market));
    }

    public List<CoinexKlineData> getKlineData(String market, String type, Long limit, String period) throws IOException {
        return checkResult(coinex.getKlineData(market, type, limit, period));
    }

    public CoinexMarketDepth getMarketDepth(String market, Long limit, String interval) throws IOException {
        return checkResult(coinex.getMarketDepth(market, limit, interval));
    }
}
