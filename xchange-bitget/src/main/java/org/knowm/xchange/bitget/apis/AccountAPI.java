package org.knowm.xchange.bitget.apis;

import java.io.IOException;
import java.util.List;

import org.knowm.xchange.bitget.model.BitgetResponse;
import org.knowm.xchange.bitget.model.dto.request.BitgetWithdrawalRequest;
import org.knowm.xchange.bitget.model.dto.response.BitgetDepositAddressResponse;
import org.knowm.xchange.bitget.model.BitgetAPIConstants;
import org.knowm.xchange.bitget.model.dto.response.BitgetDepositRecordsResponse;
import org.knowm.xchange.bitget.model.dto.response.BitgetOrderIdResponse;
import org.knowm.xchange.bitget.service.exception.BitgetApiException;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public interface AccountAPI {

  @GET
  @Path("/v2/spot/wallet/deposit-address")
  BitgetResponse<BitgetDepositAddressResponse> getDepositAddress(
      @HeaderParam(BitgetAPIConstants.ACCESS_KEY) String apiKey,
      @HeaderParam(BitgetAPIConstants.ACCESS_SIGN) ParamsDigest signature,
      @HeaderParam(BitgetAPIConstants.ACCESS_PASSPHRASE) String apiPassphrase,
      @HeaderParam(BitgetAPIConstants.ACCESS_TIMESTAMP) SynchronizedValueFactory<Long> nonce,
      @QueryParam("coin") String coin,
      @QueryParam("chain") String chain)
      throws IOException, BitgetApiException;

  @POST
  @Path("/v2/spot/wallet/withdrawal")
  @Consumes(MediaType.APPLICATION_JSON)
  BitgetResponse<BitgetOrderIdResponse> withdraw(
      @HeaderParam(BitgetAPIConstants.ACCESS_KEY) String apiKey,
      @HeaderParam(BitgetAPIConstants.ACCESS_SIGN) ParamsDigest signature,
      @HeaderParam(BitgetAPIConstants.ACCESS_PASSPHRASE) String apiPassphrase,
      @HeaderParam(BitgetAPIConstants.ACCESS_TIMESTAMP) SynchronizedValueFactory<Long> nonce,
      BitgetWithdrawalRequest bitgetWithdrawalRequest)
      throws IOException, BitgetApiException;

  @GET
  @Path("/v2/spot/wallet/deposit-records")
  BitgetResponse<List<BitgetDepositRecordsResponse>> getDepositRecords(
      @HeaderParam(BitgetAPIConstants.ACCESS_KEY) String apiKey,
      @HeaderParam(BitgetAPIConstants.ACCESS_SIGN) ParamsDigest signature,
      @HeaderParam(BitgetAPIConstants.ACCESS_PASSPHRASE) String apiPassphrase,
      @HeaderParam(BitgetAPIConstants.ACCESS_TIMESTAMP) SynchronizedValueFactory<Long> nonce,
      @QueryParam("coin") String coin,
      @QueryParam("startTime") String startTime,
      @QueryParam("endTime") String endTime,
      @QueryParam("limit") String limit)
      throws IOException, BitgetApiException;
}
