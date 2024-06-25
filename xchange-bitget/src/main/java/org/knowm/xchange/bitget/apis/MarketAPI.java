package org.knowm.xchange.bitget.apis;

import org.knowm.xchange.bitget.model.BitgetResponse;
import org.knowm.xchange.bitget.model.dto.response.BitgetCoinsResponse;
import org.knowm.xchange.bitget.model.dto.response.BitgetOrderBookResponse;
import org.knowm.xchange.bitget.model.dto.response.BitgetSymbolsResponse;
import org.knowm.xchange.bitget.service.exception.BitgetApiException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public interface MarketAPI {

  @GET
  @Path("/v2/spot/public/symbols")
  BitgetResponse<List<BitgetSymbolsResponse>> getSymbols(@QueryParam("symbol") String symbol)
      throws IOException, BitgetApiException;

  @GET
  @Path("/v2/spot/public/coins")
  BitgetResponse<List<BitgetCoinsResponse>> getCoins(@QueryParam("coin") String coin)
      throws IOException, BitgetApiException;

  @GET
  @Path("/v2/spot/market/orderbook")
  BitgetResponse<BitgetOrderBookResponse> getOrderBook(
      @QueryParam("symbol") String symbol, @QueryParam("limit") String limit)
      throws IOException, BitgetApiException;

  @GET
  @Path("/v2/spot/market/candles")
  BitgetResponse<List<String[]>> getCandles(
      @QueryParam("symbol") String symbol,
      @QueryParam("granularity") String granularity,
      @QueryParam("startTime") String startTime,
      @QueryParam("endTime") String endTime)
      throws IOException, BitgetApiException;
}
