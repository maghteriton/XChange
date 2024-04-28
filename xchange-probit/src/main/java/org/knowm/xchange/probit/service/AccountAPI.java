package org.knowm.xchange.probit.service;

import java.io.IOException;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.knowm.xchange.probit.APIConstants;
import org.knowm.xchange.probit.ProbitException;
import org.knowm.xchange.probit.dto.response.*;
import si.mazi.rescu.ParamsDigest;

@Path("/v1")
@Produces(MediaType.APPLICATION_JSON)
public interface AccountAPI {

  @GET
  @Path("/balance")
  ProbitResultDTO<List<ProbitBalanceDTO>> getBalances(
      @HeaderParam(APIConstants.AUTH_KEY) ParamsDigest authorization)
      throws IOException, ProbitException;

  @GET
  @Path("/transfer/payment")
  ProbitResultDTO<List<ProbitTransferDTO>> getDepositHistory(
      @HeaderParam(APIConstants.AUTH_KEY) ParamsDigest authorization,
      @QueryParam("currency_id") String currency,
      @QueryParam("type") String type,
      @QueryParam("status") String status,
      @QueryParam("start_time") String startTime,
      @QueryParam("end_time") String endTime,
      @QueryParam("limit") String limit)
      throws IOException, ProbitException;

  @POST
  @Path("/withdrawal")
  ProbitResultDTO<ProbitWithdrawalDTO> withdrawal(
      @HeaderParam(APIConstants.AUTH_KEY) ParamsDigest authorization,
      @QueryParam("currency_id") String currency,
      @QueryParam("platform_id") String platform,
      @QueryParam("address") String address,
      @QueryParam("destination_tag") String addressTag,
      @QueryParam("amount") String amount)
      throws IOException, ProbitException;

  @GET
  @Path("/deposit_address")
  ProbitResultDTO<List<ProbitDepositAddressDTO>> depositAddress(
          @HeaderParam(APIConstants.AUTH_KEY) ParamsDigest authorization,
          @QueryParam("currency_id") String currency)
          throws IOException, ProbitException;
}
