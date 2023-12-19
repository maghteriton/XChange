package org.knowm.xchange.mexc.v3;

import java.io.IOException;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.knowm.xchange.mexc.dto.MEXCResult;
import org.knowm.xchange.mexc.service.MEXCException;
import org.knowm.xchange.mexc.v3.dto.CoinInfo;
import org.knowm.xchange.mexc.v3.dto.ExchangeInfo;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

@Path("/api/v3")
@Produces(MediaType.APPLICATION_JSON)
public interface MEXCAuthenticatedV3 {

  @GET
  @Path("capital/config/getall")
  List<CoinInfo> getCurrencyInformation(
      @HeaderParam("x-mexc-apikey") String apiKey,
      @QueryParam("signature") ParamsDigest signature,
      @QueryParam("timestamp") SynchronizedValueFactory<Long> timestamp)
      throws IOException, MEXCException;

  @GET
  @Path("defaultSymbols")
  MEXCResult<List<String>> getDefaultSymbols()
          throws IOException, MEXCException;

  @GET
  @Path("exchangeInfo")
  ExchangeInfo getExchangeInfo()
          throws IOException, MEXCException;
}
