package org.knowm.xchange.coinex;

import java.io.IOException;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.knowm.xchange.coinex.dto.CoinexResponse;
import org.knowm.xchange.coinex.dto.account.*;
import org.knowm.xchange.coinex.dto.market.CoinexKlineData;
import org.knowm.xchange.coinex.dto.market.CoinexMarketDepth;
import org.knowm.xchange.coinex.dto.market.CoinexMarketInfo;
import org.knowm.xchange.coinex.dto.trade.CoinexCancelOrderRequest;
import org.knowm.xchange.coinex.dto.trade.CoinexOrderRequestPayload;
import org.knowm.xchange.coinex.dto.trade.CoinexOrderResponse;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

import static org.knowm.xchange.coinex.dto.APIConstants.*;

@Path("/v2")
@Produces(MediaType.APPLICATION_JSON)
public interface CoinexAuthenticated {

    @GET
    @Path("/spot/market")
    CoinexResponse<List<CoinexMarketInfo>> getMarket(
            @QueryParam("market") String market)
            throws IOException;

    @GET
    @Path("/assets/spot/balance")
    CoinexBalances getWallet(
            @HeaderParam(X_COINEX_KEY) String apiKey,
            @HeaderParam(X_COINEX_SIGN) ParamsDigest signature,
            @HeaderParam(X_COINEX_TIMESTAMP) SynchronizedValueFactory<Long> nonce)
            throws IOException;

    @GET
    @Path("/assets/info")
    CoinexResponse<List<CoinexAssets>> getAssets(
            @QueryParam("ccy") String ccy)
            throws IOException;

    @GET
    @Path("/assets/deposit-withdraw-config")
    CoinexResponse<CoinexAssetDetail> getAssetDetails(
            @QueryParam("ccy") String ccy)
            throws IOException;

    @GET
    @Path("/assets/deposit-history")
    CoinexResponse<List<CoinexDepositHistory>> getDepositHistory(
            @HeaderParam(X_COINEX_KEY) String apiKey,
            @HeaderParam(X_COINEX_SIGN) ParamsDigest signature,
            @HeaderParam(X_COINEX_TIMESTAMP) SynchronizedValueFactory<Long> nonce,
            @QueryParam("ccy") String ccy,
            @QueryParam("tx_id") String txId,
            @QueryParam("status") String status,
            @QueryParam("page") String page,
            @QueryParam("limit") String limit
    ) throws IOException;

    @GET
    @Path("/assets/deposit-address")
    CoinexResponse<CoinexDepositAddress> getDepositAddress(
            @HeaderParam(X_COINEX_KEY) String apiKey,
            @HeaderParam(X_COINEX_SIGN) ParamsDigest signature,
            @HeaderParam(X_COINEX_TIMESTAMP) SynchronizedValueFactory<Long> nonce,
            @QueryParam("ccy") String ccy,
            @QueryParam("chain") String chain
    ) throws IOException;

    @POST
    @Path("/spot/order")
    @Consumes(MediaType.APPLICATION_JSON)
    CoinexResponse<CoinexOrderResponse> placeOrder(
            @HeaderParam(X_COINEX_KEY) String apiKey,
            @HeaderParam(X_COINEX_SIGN) ParamsDigest signature,
            @HeaderParam(X_COINEX_TIMESTAMP) SynchronizedValueFactory<Long> nonce,
            CoinexOrderRequestPayload orderRequestPayload
    ) throws IOException;

    @POST
    @Path("/spot/cancel-order")
    @Consumes(MediaType.APPLICATION_JSON)
    CoinexResponse<CoinexOrderResponse> cancelOrder(
            @HeaderParam(X_COINEX_KEY) String apiKey,
            @HeaderParam(X_COINEX_SIGN) ParamsDigest signature,
            @HeaderParam(X_COINEX_TIMESTAMP) SynchronizedValueFactory<Long> nonce,
            CoinexCancelOrderRequest cancelOrderRequest
    ) throws IOException;

    @POST
    @Path("/assets/withdraw")
    @Consumes(MediaType.APPLICATION_JSON)
    CoinexResponse<CoinexWithdrawResponse> withdrawRequest(
            @HeaderParam(X_COINEX_KEY) String apiKey,
            @HeaderParam(X_COINEX_SIGN) ParamsDigest signature,
            @HeaderParam(X_COINEX_TIMESTAMP) SynchronizedValueFactory<Long> nonce,
            CoinexWithdrawRequestPayload coinexWithdrawRequestPayload
    ) throws IOException;

    @GET
    @Path("/spot/order-status")
    CoinexResponse<CoinexOrderResponse> getOrderStatus(
            @HeaderParam(X_COINEX_KEY) String apiKey,
            @HeaderParam(X_COINEX_SIGN) ParamsDigest signature,
            @HeaderParam(X_COINEX_TIMESTAMP) SynchronizedValueFactory<Long> nonce,
            @QueryParam("market") String market,
            @QueryParam("order_id") Long orderId
    ) throws IOException;

    @GET
    @Path("/spot/kline")
    CoinexResponse<List<CoinexKlineData>> getKlineData(
            @QueryParam("market") String market,
            @QueryParam("price_type") String priceType,
            @QueryParam("limit") Long limit,
            @QueryParam("period") String period
    ) throws IOException;

    @GET
    @Path("/spot/depth")
    CoinexResponse<CoinexMarketDepth> getMarketDepth(
            @QueryParam("market") String market,
            @QueryParam("limit") Long limit,
            @QueryParam("interval") String interval
    ) throws IOException;
}
