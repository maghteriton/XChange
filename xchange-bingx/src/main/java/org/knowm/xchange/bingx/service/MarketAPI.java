package org.knowm.xchange.bingx.service;

import org.knowm.xchange.bingx.BingxException;
import org.knowm.xchange.bingx.dto.BingxMarketDepthDTO;
import org.knowm.xchange.bingx.dto.BingxResultDTO;
import org.knowm.xchange.bingx.dto.wrapper.BingxSymbolWrapper;
import si.mazi.rescu.SynchronizedValueFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/openApi/spot")
@Produces(MediaType.APPLICATION_JSON)
public interface MarketAPI {


    @GET
    @Path("/v1/common/symbols")
    BingxResultDTO<BingxSymbolWrapper> getSymbols() throws IOException, BingxException;

    @GET
    @Path("/v1/market/depth")
    BingxResultDTO<BingxMarketDepthDTO> getMarketDepth(
            @QueryParam("symbol") String symbol,
            @QueryParam("timestamp") SynchronizedValueFactory<Long> timestamp
    ) throws IOException, BingxException;

    @GET
    @Path("/v2/market/kline")
    BingxResultDTO<List<List<String>>> getKLineData(
            @QueryParam("symbol") String symbol,
            @QueryParam("interval") String interval,
            @QueryParam("limit") Integer limit,
            @QueryParam("startTime") Long startTime,
            @QueryParam("endTime") Long endTime,
            @QueryParam("timestamp") SynchronizedValueFactory<Long> timestamp
    ) throws IOException, BingxException;

}
