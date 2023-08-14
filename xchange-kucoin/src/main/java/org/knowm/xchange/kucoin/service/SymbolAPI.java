/** Copyright 2019 Mek Global Limited. */
package org.knowm.xchange.kucoin.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.knowm.xchange.kucoin.dto.response.*;

/** Based on code by chenshiwei on 2019/1/11. */
@Path("api/")
@Produces(MediaType.APPLICATION_JSON)
public interface SymbolAPI {

  /**
   * Get a list of available currency pairs for trading.
   *
   * @return The available symbols.
   */
  @GET
  @Path("v1/symbols")
  KucoinResponse<List<SymbolResponse>> getSymbols() throws IOException;

  /**
   * Get a list of available currencies for trading.
   *
   * @return The available currencies.
   */
  @GET
  @Path("v1/currencies")
  KucoinResponse<List<CurrenciesResponse>> getCurrencies() throws IOException;

  @GET
  @Path("v2/currencies/{currency}")
  KucoinResponse<CurrenciesV2Response> getCurrency(
      @PathParam("currency") String currency, @QueryParam("chain") String chain) throws IOException;

  /**
   * Get the fiat price of the currencies for the available trading pairs.
   *
   * @return USD fiat price of the currencies.
   */
  @GET
  @Path("v1/prices")
  KucoinResponse<Map<String, BigDecimal>> getPrices() throws IOException;

  /**
   * Ticker include only the inside (i.e. best) bid and ask data , last price and last trade size.
   *
   * @param symbol The currency
   * @return The ticker.
   */
  @GET
  @Path("v1/market/orderbook/level1")
  KucoinResponse<TickerResponse> getTicker(@QueryParam("symbol") String symbol) throws IOException;

  /**
   * Request market tickers for all the trading pairs in the market (including 24h volume).
   *
   * @return The allTickersTickerResponse.
   */
  @GET
  @Path("v1/market/allTickers")
  KucoinResponse<AllTickersResponse> getTickers() throws IOException;

  /**
   * Get 24 hr stats for the symbol. volume is in base currency units. open, high, low are in quote
   * currency units.
   *
   * @param symbol The symbol to fetch.
   * @return The 24hr stats for the symbol.
   */
  @GET
  @Path("v1/market/stats")
  KucoinResponse<SymbolTickResponse> getMarketStats(@QueryParam("symbol") String symbol)
      throws IOException;
}
