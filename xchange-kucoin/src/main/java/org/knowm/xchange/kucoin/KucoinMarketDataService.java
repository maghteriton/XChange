package org.knowm.xchange.kucoin;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.CandleStickData;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.meta.CurrencyChainStatus;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.kucoin.dto.KlineIntervalType;
import org.knowm.xchange.kucoin.dto.response.CurrenciesV2Response;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.marketdata.params.Params;
import org.knowm.xchange.service.trade.params.CandleStickDataParams;
import org.knowm.xchange.service.trade.params.DefaultCandleStickParam;

public class KucoinMarketDataService extends KucoinMarketDataServiceRaw
    implements MarketDataService {

  /**
   * Set on calls to {@link #getOrderBook(CurrencyPair, Object...)} to return the full orderbook
   * rather than the default 100 prices either side.
   */
  public static final String PARAM_FULL_ORDERBOOK = "Full_Orderbook";

  /**
   * Set on calls to {@link #getOrderBook(CurrencyPair, Object...)} to return the shallow partial
   * orderbook depth of 20.
   */
  public static final String PARAM_PARTIAL_SHALLOW_ORDERBOOK = "Shallow_Orderbook";

  protected KucoinMarketDataService(
      KucoinExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  @Override
  public Ticker getTicker(CurrencyPair currencyPair, Object... args) throws IOException {
    return KucoinAdapters.adaptTickerFull(currencyPair, getKucoin24hrStats(currencyPair)).build();
  }

  @Override
  public List<Ticker> getTickers(Params params) throws IOException {
    return KucoinAdapters.adaptAllTickers(getKucoinTickers());
  }

  @Override
  public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args) throws IOException {
    if (Arrays.asList(args).contains(PARAM_FULL_ORDERBOOK)) {
      return KucoinAdapters.adaptOrderBook(currencyPair, getKucoinOrderBookFull(currencyPair));
    } else {
      if (Arrays.asList(args).contains(PARAM_PARTIAL_SHALLOW_ORDERBOOK)) {
        return KucoinAdapters.adaptOrderBook(
            currencyPair, getKucoinOrderBookPartialShallow(currencyPair));
      }
      return KucoinAdapters.adaptOrderBook(currencyPair, getKucoinOrderBookPartial(currencyPair));
    }
  }

  @Override
  public Trades getTrades(CurrencyPair currencyPair, Object... args) throws IOException {
    return KucoinAdapters.adaptTrades(currencyPair, getKucoinTrades(currencyPair));
  }

  @Override
  public CandleStickData getCandleStickData(CurrencyPair currencyPair, CandleStickDataParams params)
      throws IOException {

    if (!(params instanceof DefaultCandleStickParam)) {
      throw new NotYetImplementedForExchangeException("Only DefaultCandleStickParam is supported");
    }

    DefaultCandleStickParam defaultCandleStickParam = (DefaultCandleStickParam) params;
    long startAt = defaultCandleStickParam.getEndDate().getTime() / 1000;
    long endAt = defaultCandleStickParam.getStartDate().getTime() / 1000;
    long periodInSecs = defaultCandleStickParam.getPeriodInSecs();
    KlineIntervalType interval = KlineIntervalType.min30;
    for (KlineIntervalType kucoinKlineInterval : KlineIntervalType.values()) {
      if (kucoinKlineInterval.getSeconds() == periodInSecs) {
        interval = kucoinKlineInterval;
        break;
      }
    }

    return KucoinAdapters.adaptCandleStickData(
        currencyPair, getKucoinKlines(currencyPair, startAt, endAt, interval));
  }

  @Override
  public CurrencyChainStatus getCurrencyChainStatus(Currency currency, String chain)
      throws IOException {
    CurrenciesV2Response currenciesV2Response = getKucoinCurrency(currency, chain.toLowerCase());
    if (currenciesV2Response != null && currenciesV2Response.getChains() != null) {
      for (KucoinChain kucoinChain : currenciesV2Response.getChains()) {
        if (kucoinChain.getChain().toUpperCase().contains(chain.toUpperCase())) {
          return new CurrencyChainStatus(
              currency,
              kucoinChain.getChain(),
              kucoinChain.getIsDepositEnabled(),
              kucoinChain.getIsWithdrawEnabled());
        }
      }
    }

    return null; // returns null if not found
  }
}
