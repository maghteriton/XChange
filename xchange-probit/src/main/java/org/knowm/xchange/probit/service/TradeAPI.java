package org.knowm.xchange.probit.service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.knowm.xchange.probit.APIConstants;
import org.knowm.xchange.probit.dto.request.ProbitCancelOrderRequestDTO;
import org.knowm.xchange.probit.dto.response.ProbitLimitOrderDTO;
import org.knowm.xchange.probit.dto.request.ProbitNewOrderRequestDTO;
import org.knowm.xchange.probit.dto.response.ProbitResultDTO;
import si.mazi.rescu.ParamsDigest;

import java.util.List;

@Path("/v1")
@Produces(MediaType.APPLICATION_JSON)
public interface TradeAPI {

  @POST
  @Path("/new_order")
  @Consumes(MediaType.APPLICATION_JSON)
  ProbitResultDTO<ProbitLimitOrderDTO> newOrder(
      @HeaderParam(APIConstants.AUTH_KEY) ParamsDigest authorization,
      ProbitNewOrderRequestDTO probitNewOrderRequestDTO);

  @POST
  @Path("/cancel_order")
  @Consumes(MediaType.APPLICATION_JSON)
  ProbitResultDTO<ProbitLimitOrderDTO> cancelOrder(
      @HeaderParam(APIConstants.AUTH_KEY) ParamsDigest authorization,
      ProbitCancelOrderRequestDTO probitCancelOrderRequestDTO);

  @GET
  @Path("/order")
  ProbitResultDTO<List<ProbitLimitOrderDTO>> getOrder(
      @HeaderParam(APIConstants.AUTH_KEY) ParamsDigest authorization,
      @QueryParam("market_id") String currency,
      @QueryParam("order_id") String orderId);
}
