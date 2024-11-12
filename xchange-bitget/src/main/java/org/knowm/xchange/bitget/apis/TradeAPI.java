package org.knowm.xchange.bitget.apis;

import org.knowm.xchange.bitget.model.BitgetAPIConstants;
import org.knowm.xchange.bitget.model.BitgetResponse;
import org.knowm.xchange.bitget.model.dto.request.BitgetCancelLimitOrderRequest;
import org.knowm.xchange.bitget.model.dto.request.BitgetLimitOrderRequest;
import org.knowm.xchange.bitget.model.dto.response.BitgetOrderHistoryResponse;
import org.knowm.xchange.bitget.model.dto.response.BitgetOrderIdResponse;
import org.knowm.xchange.bitget.service.exception.BitgetApiException;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public interface TradeAPI {

  @POST
  @Path("/v2/spot/trade/place-order")
  @Consumes(MediaType.APPLICATION_JSON)
  BitgetResponse<BitgetOrderIdResponse> placeLimitOrder(
      @HeaderParam(BitgetAPIConstants.ACCESS_KEY) String apiKey,
      @HeaderParam(BitgetAPIConstants.ACCESS_SIGN) ParamsDigest signature,
      @HeaderParam(BitgetAPIConstants.ACCESS_PASSPHRASE) String apiPassphrase,
      @HeaderParam(BitgetAPIConstants.ACCESS_TIMESTAMP) SynchronizedValueFactory<Long> nonce,
      BitgetLimitOrderRequest bitgetLimitOrderRequest)
      throws IOException, BitgetApiException;

  @POST
  @Path("/v2/spot/trade/cancel-order")
  @Consumes(MediaType.APPLICATION_JSON)
  BitgetResponse<BitgetOrderIdResponse> cancelLimitOrder(
      @HeaderParam(BitgetAPIConstants.ACCESS_KEY) String apiKey,
      @HeaderParam(BitgetAPIConstants.ACCESS_SIGN) ParamsDigest signature,
      @HeaderParam(BitgetAPIConstants.ACCESS_PASSPHRASE) String apiPassphrase,
      @HeaderParam(BitgetAPIConstants.ACCESS_TIMESTAMP) SynchronizedValueFactory<Long> nonce,
      BitgetCancelLimitOrderRequest bitgetCancelLimitOrderRequest)
      throws IOException, BitgetApiException;

  @GET
  @Path("/v2/spot/trade/history-orders")
  BitgetResponse<List<BitgetOrderHistoryResponse>> getOrderHistory(
      @HeaderParam(BitgetAPIConstants.ACCESS_KEY) String apiKey,
      @HeaderParam(BitgetAPIConstants.ACCESS_SIGN) ParamsDigest signature,
      @HeaderParam(BitgetAPIConstants.ACCESS_PASSPHRASE) String apiPassphrase,
      @HeaderParam(BitgetAPIConstants.ACCESS_TIMESTAMP) SynchronizedValueFactory<Long> nonce,
      @QueryParam("symbol") String symbol,
      @QueryParam("orderId") String orderId)
      throws IOException, BitgetApiException;


  @GET
  @Path("/v2/spot/trade/orderInfo")
  BitgetResponse<List<BitgetOrderHistoryResponse>> getOrderInfo(
          @HeaderParam(BitgetAPIConstants.ACCESS_KEY) String apiKey,
          @HeaderParam(BitgetAPIConstants.ACCESS_SIGN) ParamsDigest signature,
          @HeaderParam(BitgetAPIConstants.ACCESS_PASSPHRASE) String apiPassphrase,
          @HeaderParam(BitgetAPIConstants.ACCESS_TIMESTAMP) SynchronizedValueFactory<Long> nonce,
          @QueryParam("orderId") String orderId)
          throws IOException, BitgetApiException;
}
