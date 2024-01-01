package org.knowm.xchange.bingx.service;

import org.knowm.xchange.bingx.APIConstants;
import org.knowm.xchange.bingx.BingxException;
import org.knowm.xchange.bingx.dto.BingxDepositDTO;
import org.knowm.xchange.bingx.dto.BingxResultDTO;
import org.knowm.xchange.bingx.dto.wrapper.BingxBalancesWrapper;
import org.knowm.xchange.bingx.dto.BingxWalletDTO;
import org.knowm.xchange.bingx.dto.wrapper.BingxDepositAddressesWrapper;
import org.knowm.xchange.bingx.dto.wrapper.BingxWithdrawWrapper;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Path("/openApi")
@Produces(MediaType.APPLICATION_JSON)
public interface AccountAPI {

  @GET
  @Path("/spot/v1/account/balance")
  BingxResultDTO<BingxBalancesWrapper> getBalances(
      @HeaderParam(APIConstants.API_HEADER_KEY) String apiKey,
      @QueryParam("timestamp") SynchronizedValueFactory<Long> timestamp,
      @QueryParam("recvWindow") Integer recvWindow,
      @QueryParam("signature") ParamsDigest signature)
      throws IOException, BingxException;

  @GET
  @Path("/api/v3/capital/deposit/hisrec")
  List<BingxDepositDTO> getDepositHistory(
      @HeaderParam(APIConstants.API_HEADER_KEY) String apiKey,
      @QueryParam("timestamp") SynchronizedValueFactory<Long> timestamp,
      @QueryParam("recvWindow") Integer recvWindow,
      @QueryParam("signature") ParamsDigest signature,
      @QueryParam("coin") String coin)
      throws IOException, BingxException;

  @GET
  @Path("/wallets/v1/capital/config/getall")
  BingxResultDTO<List<BingxWalletDTO>> getWallets(
      @HeaderParam(APIConstants.API_HEADER_KEY) String apiKey,
      @QueryParam("timestamp") SynchronizedValueFactory<Long> timestamp,
      @QueryParam("recvWindow") Integer recvWindow,
      @QueryParam("signature") ParamsDigest signature,
      @QueryParam("coin") String coin)
      throws IOException, BingxException;

  // wallets/v1/capital/deposit/address

  @GET
  @Path("/wallets/v1/capital/deposit/address")
  BingxResultDTO<BingxDepositAddressesWrapper> getDepositAddresses(
      @HeaderParam(APIConstants.API_HEADER_KEY) String apiKey,
      @QueryParam("timestamp") SynchronizedValueFactory<Long> timestamp,
      @QueryParam("recvWindow") Integer recvWindow,
      @QueryParam("signature") ParamsDigest signature,
      @QueryParam("coin") String coin)
      throws IOException, BingxException;

  @POST
  @Path("/wallets/v1/capital/withdraw/apply")
  BingxResultDTO<BingxWithdrawWrapper> withdraw(
      @HeaderParam(APIConstants.API_HEADER_KEY) String apiKey,
      @QueryParam("timestamp") SynchronizedValueFactory<Long> timestamp,
      @QueryParam("recvWindow") Integer recvWindow,
      @QueryParam("signature") ParamsDigest signature,
      @QueryParam("address") String address,
      @QueryParam("addressTag") String addressTag,
      @QueryParam("amount") BigDecimal amount,
      @QueryParam("coin") String coin,
      @QueryParam("network") String network,
      @QueryParam("walletType") String walletType)
      throws IOException, BingxException;
}
