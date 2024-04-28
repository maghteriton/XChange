package org.knowm.xchange.probit.service.market;

import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.CandleStickData;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.probit.ProbitAdapter;
import org.knowm.xchange.probit.ProbitException;
import org.knowm.xchange.probit.ProbitExchange;
import org.knowm.xchange.probit.dto.response.*;
import org.knowm.xchange.probit.model.KLineInterval;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.params.CandleStickDataParams;
import org.knowm.xchange.service.trade.params.DefaultCandleStickParam;

import java.io.IOException;
import java.util.List;

public class ProbitMarketDataService extends ProbitMarketDataServiceRaw
    implements MarketDataService {

  public ProbitMarketDataService(
      ProbitExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public List<ProbitMarketDTO> getMarket() throws IOException {
    List<ProbitMarketDTO> market;
    try {
      market = getMarketData().getData();
    } catch (ProbitException e) {
      throw new ExchangeException(
          String.format(
              "Code: %s, Message: %s, Details: %s",
              e.getErrorCode(), e.getMessage(), e.getDetails()));
    }

    return market;
  }

  public List<ProbitCurrencyDTO> getCurrency() throws IOException {
    List<ProbitCurrencyDTO> market;
    try {
      market = getCurrencyData().getData();
    } catch (ProbitException e) {
      throw new ExchangeException(
          String.format(
              "Code: %s, Message: %s, Details: %s",
              e.getErrorCode(), e.getMessage(), e.getDetails()));
    }

    return market;
  }

  @Override
  public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args) throws IOException {
    List<ProbitOrderBookDTO> probitOrderBook;
    try {
      probitOrderBook = getOrderBookData(currencyPair).getData();
    } catch (ProbitException e) {
      throw new ExchangeException(
          String.format(
              "Code: %s, Message: %s, Details: %s",
              e.getErrorCode(), e.getMessage(), e.getDetails()));
    }

    return ProbitAdapter.adaptOrderBook(currencyPair, probitOrderBook);
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
    KLineInterval interval = KLineInterval.min30;
    for (KLineInterval probitInterval : KLineInterval.values()) {
      if (probitInterval.getSeconds() == periodInSecs) {
        interval = probitInterval;
        break;
      }
    }

    ProbitResultDTO<List<ProbitCandleDTO>> candleData =
        getCandleData(
            currencyPair,
            defaultCandleStickParam.getStartDate(),
            defaultCandleStickParam.getEndDate(),
            interval.code());
    return ProbitAdapter.adaptCandleStickData(currencyPair, candleData.getData());
  }
}
