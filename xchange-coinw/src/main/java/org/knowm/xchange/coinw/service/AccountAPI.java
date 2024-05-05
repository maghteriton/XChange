package org.knowm.xchange.coinw.service;

import java.io.IOException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.knowm.xchange.coinw.APIConstants;
import org.knowm.xchange.coinw.CoinwException;
import org.knowm.xchange.coinw.dto.request.CoinwDepositAddressRequestDTO;
import org.knowm.xchange.coinw.dto.response.CoinwDepositAddressResponseDTO;
import org.knowm.xchange.coinw.dto.CoinwResultDTO;
import si.mazi.rescu.ParamsDigest;

@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
public interface AccountAPI {

  @POST
  @Path("/private?command=returnDepositAddresses")
  @Consumes(MediaType.APPLICATION_JSON)
  CoinwResultDTO<CoinwDepositAddressResponseDTO> getDepositAddress(
          @HeaderParam(APIConstants.API_KEY) String apiKey,
          @HeaderParam(APIConstants.SIGN) ParamsDigest authorization,
          CoinwDepositAddressRequestDTO depositAddressRequestDTO)
          throws IOException, CoinwException;
}
