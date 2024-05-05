package org.knowm.xchange.coinw.service.account;

import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.coinw.CoinwExchange;
import org.knowm.xchange.coinw.CoinwResilience;
import org.knowm.xchange.coinw.dto.CoinwResultDTO;
import org.knowm.xchange.coinw.dto.request.CoinwDepositAddressRequestDTO;
import org.knowm.xchange.coinw.dto.response.CoinwDepositAddressResponseDTO;
import org.knowm.xchange.coinw.service.CoinwBaseService;
import org.knowm.xchange.currency.Currency;

import java.io.IOException;
import java.util.List;

public class CoinwAccountServiceRaw extends CoinwBaseService {

  public CoinwAccountServiceRaw(CoinwExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public CoinwResultDTO<CoinwDepositAddressResponseDTO> getDepositAddressData(
      Currency currency, String chain) throws IOException {

    CoinwDepositAddressRequestDTO depositAddressRequestDTO =
        CoinwDepositAddressRequestDTO.builder()
            .symbolId(currency.getCurrencyCode())
            .chain(chain)
            .build();

    return decorateApiCall(
            () ->
                accountAPI.getDepositAddress(
                    apiKey, signatureCreator, depositAddressRequestDTO))
        .withRetry(retry("getDepositAddress"))
        .withRateLimiter(rateLimiter(CoinwResilience.PRIVATE_RATE_LIMITS))
        .call();
  }
}
