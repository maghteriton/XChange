package org.knowm.xchange.coinex.service.market;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.coinex.CoinexAdapters;
import org.knowm.xchange.coinex.dto.CoinexPeriod;
import org.knowm.xchange.coinex.dto.market.CoinexKlineData;
import org.knowm.xchange.coinex.dto.market.CoinexMarketDepth;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.CandleStickData;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.params.CandleStickDataParams;
import org.knowm.xchange.service.trade.params.DefaultCandleStickParam;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class CoinexMarketDataService extends CoinexMarketServiceRaw implements MarketDataService {
    public CoinexMarketDataService(Exchange exchange) {
        super(exchange);
    }

    @Override
    public CandleStickData getCandleStickData(CurrencyPair currencyPair, CandleStickDataParams params) throws IOException {

        if (!(params instanceof DefaultCandleStickParam)) {
            throw new NotYetImplementedForExchangeException("Only DefaultCandleStickParam is supported");
        }

        DefaultCandleStickParam defaultCandleStickParam = (DefaultCandleStickParam) params;
        long periodInSecs = defaultCandleStickParam.getPeriodInSecs();
        CoinexPeriod interval = CoinexPeriod._30MIN;
        for (CoinexPeriod coinexPeriod : CoinexPeriod.values()) {
            if (coinexPeriod.getSeconds() == periodInSecs) {
                interval = coinexPeriod;
                break;
            }
        }
        Date endDate = defaultCandleStickParam.getEndDate();
        Date startDate = defaultCandleStickParam.getStartDate();
        long secondsBetween = Math.abs((endDate.getTime() - startDate.getTime()) / 1000);
        long limit = secondsBetween / interval.getSeconds();

        List<CoinexKlineData> klineDataResponse = getKlineData(
                CoinexAdapters.getMarket(currencyPair),
                null,
                limit,
                interval.getPeriod());

        return CoinexAdapters.adaptCandleStickData(currencyPair, klineDataResponse);
    }

    @Override
    public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args) throws IOException {
        InstrumentMetaData instrumentMetaData = exchange.getExchangeMetaData().getInstruments().get(currencyPair);
        Integer volumeScale = instrumentMetaData.getVolumeScale();
        BigDecimal volumeStepSize = BigDecimal.ONE.scaleByPowerOfTen(-volumeScale);
        CoinexMarketDepth marketDepth = getMarketDepth(
                CoinexAdapters.getMarket(currencyPair),
                50L,
                volumeStepSize.toPlainString());

        return CoinexAdapters.adaptMarketDepth(currencyPair, marketDepth);
    }
}
