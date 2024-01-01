package org.knowm.xchange.bingx.service;

import org.knowm.xchange.bingx.APIConstants;
import org.knowm.xchange.bingx.BingxException;
import org.knowm.xchange.bingx.dto.BingxOrderDTO;
import org.knowm.xchange.bingx.dto.BingxResultDTO;
import org.knowm.xchange.bingx.dto.TradeCommissionRateDTO;
import org.knowm.xchange.bingx.dto.wrapper.BingxCancelLimitOrderWrapper;
import org.knowm.xchange.bingx.dto.wrapper.BingxCreateLimitOrderWrapper;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.math.BigDecimal;

@Path("/openApi/spot")
@Produces(MediaType.APPLICATION_JSON)
public interface TradeAPI {

  @GET
  @Path("/v1/user/commissionRate")
  BingxResultDTO<TradeCommissionRateDTO> getCommissionRate(
      @HeaderParam(APIConstants.API_HEADER_KEY) String apiKey,
      @QueryParam("timestamp") SynchronizedValueFactory<Long> timestamp,
      @QueryParam("recvWindow") Integer recvWindow,
      @QueryParam("signature") ParamsDigest signature,
      @QueryParam("symbol") String symbol)
      throws IOException, BingxException;

  @GET
  @Path("/v1/trade/query")
  BingxResultDTO<BingxOrderDTO> queryOrder(
      @HeaderParam(APIConstants.API_HEADER_KEY) String apiKey,
      @QueryParam("timestamp") SynchronizedValueFactory<Long> timestamp,
      @QueryParam("recvWindow") Integer recvWindow,
      @QueryParam("signature") ParamsDigest signature,
      @QueryParam("symbol") String symbol,
      @QueryParam("orderId") String orderId)
      throws IOException, BingxException;

  @POST
  @Path("/v1/trade/order")
  BingxResultDTO<BingxCreateLimitOrderWrapper> createLimitOrder(
      @HeaderParam(APIConstants.API_HEADER_KEY) String apiKey,
      @QueryParam("timestamp") SynchronizedValueFactory<Long> timestamp,
      @QueryParam("recvWindow") Integer recvWindow,
      @QueryParam("signature") ParamsDigest signature,
      @QueryParam("symbol") String symbol,
      @QueryParam("side") String side,
      @QueryParam("type") String type,
      @QueryParam("quantity") BigDecimal quantity,
      @QueryParam("price") BigDecimal price)
      throws IOException, BingxException;

  @POST
  @Path("/v1/trade/cancel")
  BingxResultDTO<BingxCancelLimitOrderWrapper> cancelLimitOrder(
      @HeaderParam(APIConstants.API_HEADER_KEY) String apiKey,
      @QueryParam("timestamp") SynchronizedValueFactory<Long> timestamp,
      @QueryParam("recvWindow") Integer recvWindow,
      @QueryParam("signature") ParamsDigest signature,
      @QueryParam("symbol") String symbol,
      @QueryParam("orderId") String orderId)
      throws IOException, BingxException;
}
