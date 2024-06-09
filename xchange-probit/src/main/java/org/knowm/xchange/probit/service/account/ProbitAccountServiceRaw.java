package org.knowm.xchange.probit.service.account;

import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.probit.ProbitAdapter;
import org.knowm.xchange.probit.ProbitExchange;
import org.knowm.xchange.probit.dto.request.ProbitWithdrawalRequestDTO;
import org.knowm.xchange.probit.dto.response.*;
import org.knowm.xchange.probit.model.ProbitStatus;
import org.knowm.xchange.probit.model.ProbitTransferType;
import org.knowm.xchange.probit.service.ProbitBaseService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.knowm.xchange.probit.ProbitResilience.GROUP_1_ENDPOINT_RATE_LIMITER;
import static org.knowm.xchange.probit.ProbitResilience.GROUP_3_ENDPOINT_RATE_LIMITER;

public class ProbitAccountServiceRaw extends ProbitBaseService {

  public ProbitAccountServiceRaw(
      ProbitExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public ProbitResultDTO<List<ProbitBalanceDTO>> getBalances() throws IOException {
    return decorateApiCall(() -> accountAPI.getBalances(signatureCreator))
        .withRetry(retry("getBalances"))
        .withRateLimiter(rateLimiter(GROUP_3_ENDPOINT_RATE_LIMITER))
        .call();
  }

  public ProbitResultDTO<List<ProbitTransferDTO>> getTransfers(
      Currency currency,
      ProbitTransferType probitTransferType,
      ProbitStatus probitStatus,
      Date startTime,
      Date endTime,
      Integer limit)
      throws IOException {

    return decorateApiCall(
            () ->
                accountAPI.getDepositHistory(
                    signatureCreator,
                    currency.getCurrencyCode(),
                    probitTransferType != null ? probitTransferType.getType() : null,
                    probitStatus != null ? probitStatus.getStatus() : null,
                    ProbitAdapter.formatDate(startTime),
                    ProbitAdapter.formatDate(endTime),
                    String.valueOf(limit)))
        .withRetry(retry("getTransfers"))
        .withRateLimiter(rateLimiter(GROUP_1_ENDPOINT_RATE_LIMITER))
        .call();
  }

  public ProbitResultDTO<List<ProbitDepositAddressDTO>> getPlatformDepositAddress(Currency currency, String platform)
      throws IOException {

    return decorateApiCall(
            () -> accountAPI.depositAddress(signatureCreator, currency.getCurrencyCode(), platform))
        .withRetry(retry("getDepositAddress"))
        .withRateLimiter(rateLimiter(GROUP_3_ENDPOINT_RATE_LIMITER))
        .call();
  }

  public ProbitResultDTO<ProbitWithdrawalDTO> withdrawal(
      ProbitWithdrawalRequestDTO probitWithdrawalRequestDTO) throws IOException {

    return decorateApiCall(
            () -> accountAPI.withdrawal(signatureCreator, probitWithdrawalRequestDTO))
        .withRateLimiter(rateLimiter(GROUP_1_ENDPOINT_RATE_LIMITER))
        .call();
  }
}
