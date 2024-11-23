package org.knowm.xchange.coinex;

import java.io.IOException;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.knowm.xchange.coinex.dto.CoinexResponse;
import org.knowm.xchange.coinex.dto.account.CoinexAssetDetail;
import org.knowm.xchange.coinex.dto.account.CoinexAssets;
import org.knowm.xchange.coinex.dto.account.CoinexBalances;
import org.knowm.xchange.coinex.dto.market.CoinexMarketInfo;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

import static org.knowm.xchange.coinex.dto.APIConstants.*;

@Path("/v2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CoinexAuthenticated {

    @GET
    @Path("/assets/spot/balance")
    CoinexBalances getWallet(
            @HeaderParam(X_COINEX_KEY) String apiKey,
            @HeaderParam(X_COINEX_SIGN) ParamsDigest signature,
            @HeaderParam(X_COINEX_TIMESTAMP) SynchronizedValueFactory<Long> nonce)
            throws IOException;


    @GET
    @Path("/spot/market")
    CoinexResponse<List<CoinexMarketInfo>> getMarket(
            @QueryParam("market") String market)
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
}
