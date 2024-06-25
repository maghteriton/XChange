package org.knowm.xchange.bitget.service;

import org.knowm.xchange.bitget.BitgetAdapter;
import org.knowm.xchange.bitget.BitgetExchange;
import org.knowm.xchange.bitget.model.enums.BitgetKlineIntervalType;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.CandleStickData;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.params.CandleStickDataParams;
import org.knowm.xchange.service.trade.params.DefaultCandleStickParam;

import java.io.IOException;

public class BitgetMarketDataService extends BitgetMarketDataServiceRaw
    implements MarketDataService {

  public BitgetMarketDataService(
      BitgetExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  @Override
  public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args) throws IOException {
    return BitgetAdapter.adaptOrderBook(
        currencyPair, getBitgetOrderBook(BitgetAdapter.convertToSymbol(currencyPair)));
  }

  @Override
  public CandleStickData getCandleStickData(CurrencyPair currencyPair, CandleStickDataParams params)
      throws IOException {

    if (!(params instanceof DefaultCandleStickParam)) {
      throw new NotYetImplementedForExchangeException(
          "Only DefaultCandleStickParamWithLimit is supported");
    }

    DefaultCandleStickParam defaultCandleStickParam = (DefaultCandleStickParam) params;
    long periodInSecs = defaultCandleStickParam.getPeriodInSecs();
    BitgetKlineIntervalType interval = BitgetKlineIntervalType.min30;
    for (BitgetKlineIntervalType bitgetKlineIntervalType : BitgetKlineIntervalType.values()) {
      if (bitgetKlineIntervalType.getSeconds() == periodInSecs) {
        interval = bitgetKlineIntervalType;
        break;
      }
    }

    String bitgetEndTime = String.valueOf(defaultCandleStickParam.getStartDate().getTime());
    String bitgetStartTime = String.valueOf(defaultCandleStickParam.getEndDate().getTime());
    return BitgetAdapter.adaptCandleStickData(
        currencyPair,
        getBitgetCandles(
            BitgetAdapter.convertToSymbol(currencyPair),
            interval.code(),
            bitgetStartTime,
            bitgetEndTime));
  }
}
