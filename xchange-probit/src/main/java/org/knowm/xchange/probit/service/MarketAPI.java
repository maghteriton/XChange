package org.knowm.xchange.probit.service;

import org.knowm.xchange.probit.ProbitException;
import org.knowm.xchange.probit.dto.response.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/v1")
@Produces(MediaType.APPLICATION_JSON)
public interface MarketAPI {

  @GET
  @Path("/market")
  ProbitResultDTO<List<ProbitMarketDTO>> getMarket() throws IOException, ProbitException;

  @GET
  @Path("/currency")
  ProbitResultDTO<List<ProbitCurrencyDTO>> getCurrency();

  @GET
  @Path("/order_book")
  ProbitResultDTO<List<ProbitOrderBookDTO>> getOrderBook(
      @QueryParam("market_id") String currency);

  @GET
  @Path("/candle")
  ProbitResultDTO<List<ProbitCandleDTO>> getCandle(
          @QueryParam("market_ids") String currency,
          @QueryParam("start_time") String startTime,
          @QueryParam("end_time") String endTime,
          @QueryParam("interval") String interval,
          @QueryParam("sort") String sort,
          @QueryParam("limit") String limit
  );
}
