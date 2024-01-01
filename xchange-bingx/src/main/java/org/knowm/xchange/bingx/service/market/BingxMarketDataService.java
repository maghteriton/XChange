package org.knowm.xchange.bingx.service.market;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.bingx.BingxException;
import org.knowm.xchange.bingx.KLineInterval;
import org.knowm.xchange.bingx.dto.BingxMarketDepthDTO;
import org.knowm.xchange.bingx.dto.BingxResultDTO;
import org.knowm.xchange.bingx.dto.wrapper.BingxSymbolWrapper;
import org.knowm.xchange.bingx.dto.BingxSymbolDTO;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class BingxMarketDataService extends BingxMarketDataServiceRaw implements MarketDataService {
  public BingxMarketDataService(Exchange exchange) {
    super(exchange);
  }

  public List<BingxSymbolDTO> getSymbols() throws IOException {
    BingxResultDTO<BingxSymbolWrapper> symbols;
    try {
      symbols = marketAPI.getSymbols();
    } catch (BingxException e) {
      throw new ExchangeException(e);
    }
    return symbols.getData().getSymbols();
  }

  public BingxMarketDepthDTO getMarketDepth(String symbol) throws IOException {
    BingxResultDTO<BingxMarketDepthDTO> marketDepths;
    try {
      marketDepths = marketAPI.getMarketDepth(symbol, nonceFactory);
    } catch (BingxException e) {
      throw new ExchangeException(e);
    }
    return marketDepths.getData();
  }

  public List<List<String>> getKLineData(
      String symbol, KLineInterval interval, Integer limit, Date startTime, Date endTime)
      throws IOException {
    BingxResultDTO<List<List<String>>> kLineData;
    try {
      Long startTimeAsMs = startTime != null ? startTime.getTime() : null;
      Long endTimeAsMs = endTime != null ? endTime.getTime() : null;

      kLineData = marketAPI.getKLineData(symbol, interval.code(), limit, startTimeAsMs, endTimeAsMs, nonceFactory);
    } catch (BingxException e) {
      throw new ExchangeException(e);
    }
    return kLineData.getData();
  }
}
