package org.knowm.xchange.mexc;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.knowm.xchange.mexc.dto.MEXCResult;
import org.knowm.xchange.mexc.dto.account.*;
import org.knowm.xchange.mexc.dto.market.MEXCCurrencyInfo;
import org.knowm.xchange.mexc.dto.market.MEXCDepth;
import org.knowm.xchange.mexc.dto.market.MEXCSymbols;
import org.knowm.xchange.mexc.dto.trade.MEXCOrder;
import org.knowm.xchange.mexc.dto.trade.MEXCOrderRequestPayload;
import org.knowm.xchange.mexc.service.MEXCException;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

@Path("/open/api/v2")
@Produces(MediaType.APPLICATION_JSON)
public interface MEXCAuthenticated {

  @GET
  @Path("/market/symbols")
  MEXCResult<List<MEXCSymbols>> getSymbols() throws IOException, MEXCException;

  @GET
  @Path("market/coin/list")
  MEXCResult<List<MEXCCurrencyInfo>> getCoinList(@QueryParam("currency") String currency)
      throws IOException, MEXCException;

  @GET
  @Path("/account/info")
  MEXCResult<Map<String, MEXCBalance>> getWalletBalances(
      @HeaderParam("ApiKey") String apiKey,
      @HeaderParam("Request-Time") SynchronizedValueFactory<Long> timestamp,
      @HeaderParam("Signature") ParamsDigest signature)
      throws IOException, MEXCException;

  @POST
  @Path("/order/place")
  @Consumes(MediaType.APPLICATION_JSON)
  MEXCResult<String> placeOrder(
      @HeaderParam("ApiKey") String apiKey,
      @HeaderParam("Request-Time") SynchronizedValueFactory<Long> timestamp,
      @HeaderParam("Signature") ParamsDigest signature,
      MEXCOrderRequestPayload orderRequestPayload)
      throws IOException, MEXCException;

  @GET
  @Path("/order/query")
  MEXCResult<List<MEXCOrder>> getOrders(
      @HeaderParam("ApiKey") String apiKey,
      @HeaderParam("Request-Time") SynchronizedValueFactory<Long> timestamp,
      @HeaderParam("Signature") ParamsDigest signature,
      @QueryParam("order_ids") List<String> orderIds)
      throws IOException, MEXCException;

  @GET
  @Path("/market/depth")
  MEXCResult<MEXCDepth> getMarketDepth(
      @HeaderParam("ApiKey") String apiKey,
      @HeaderParam("Request-Time") SynchronizedValueFactory<Long> timestamp,
      @HeaderParam("Signature") ParamsDigest signature,
      @QueryParam("symbol") String symbol,
      @QueryParam("depth") String depth)
      throws IOException, MEXCException;

  @POST
  @Path("/asset/withdraw")
  @Consumes(MediaType.APPLICATION_JSON)
  MEXCResult<MEXCWithdrawal> withdrawFunds(
      @HeaderParam("ApiKey") String apiKey,
      @HeaderParam("Request-Time") SynchronizedValueFactory<Long> timestamp,
      @HeaderParam("Signature") ParamsDigest signature,
      MEXCWithdrawRequestPayload mexcWithdrawRequestPayload)
      throws IOException, MEXCException;

  @GET
  @Path("/asset/deposit/list")
  MEXCResult<MEXCAssetTransferHistory<MEXCDepositRecord>> getDepositList(
      @HeaderParam("ApiKey") String apiKey,
      @HeaderParam("Request-Time") SynchronizedValueFactory<Long> timestamp,
      @HeaderParam("Signature") ParamsDigest signature)
      throws IOException, MEXCException;

  @GET
  @Path("/asset/withdraw/list")
  MEXCResult<MEXCAssetTransferHistory<MEXCWithdrawalRecord>> getWithdrawList(
      @HeaderParam("ApiKey") String apiKey,
      @HeaderParam("Request-Time") SynchronizedValueFactory<Long> timestamp,
      @HeaderParam("Signature") ParamsDigest signature)
      throws IOException, MEXCException;

  @GET
  @Path("/asset/deposit/address/list")
  MEXCResult<MEXCDepositAddress> getDepositAddressList(
      @HeaderParam("ApiKey") String apiKey,
      @HeaderParam("Request-Time") SynchronizedValueFactory<Long> timestamp,
      @HeaderParam("Signature") ParamsDigest signature,
      @QueryParam("currency") String currency)
      throws IOException, MEXCException;
}
