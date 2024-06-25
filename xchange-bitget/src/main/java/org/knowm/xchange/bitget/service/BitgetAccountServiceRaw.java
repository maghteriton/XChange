package org.knowm.xchange.bitget.service;

import org.knowm.xchange.bitget.BitgetBaseService;
import org.knowm.xchange.bitget.BitgetExchange;
import org.knowm.xchange.bitget.model.dto.request.BitgetWithdrawalRequest;
import org.knowm.xchange.bitget.model.dto.response.BitgetCoinsResponse;
import org.knowm.xchange.bitget.model.dto.response.BitgetDepositAddressResponse;
import org.knowm.xchange.bitget.model.dto.response.BitgetDepositRecordsResponse;
import org.knowm.xchange.bitget.model.dto.response.BitgetOrderIdResponse;
import org.knowm.xchange.bitget.service.exception.BitgetApiException;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.dto.account.AddressWithTag;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.knowm.xchange.bitget.BitgetResilience.PUBLIC_REST_ENDPOINT_RATE_LIMITER;
import static org.knowm.xchange.bitget.service.exception.BitgetExceptionClassifier.classifyingExceptions;
import static org.knowm.xchange.bitget.BitgetResilience.PRIVATE_REST_ENDPOINT_RATE_LIMITER;

public class BitgetAccountServiceRaw extends BitgetBaseService {

  private static final String TRANSFER_TYPE = "on_chain";

  protected BitgetAccountServiceRaw(
      BitgetExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public BitgetDepositAddressResponse getDepositAddress(String coin, String chain)
      throws IOException {
    checkAuthenticated();
    return classifyingExceptions(
        () ->
            decorateApiCall(
                    () ->
                        accountAPI.getDepositAddress(
                            apiKey, digest, passphrase, nonceFactory, coin, chain))
                .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }

  public BitgetOrderIdResponse withdraw(
      String coin, String chain, BigDecimal amount, AddressWithTag addressWithTag)
      throws IOException {
    checkAuthenticated();

    BitgetWithdrawalRequest withdrawalRequest =
        BitgetWithdrawalRequest.builder()
            .coin(coin)
            .address(addressWithTag.getAddress())
            .transferType(TRANSFER_TYPE)
            .tag(addressWithTag.getAddressTag())
            .chain(chain)
            .size(amount.toPlainString())
            .build();

    return classifyingExceptions(
        () ->
            decorateApiCall(
                    () ->
                        accountAPI.withdraw(
                            apiKey, digest, passphrase, nonceFactory, withdrawalRequest))
                .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }

  public List<BitgetDepositRecordsResponse> getDepositRecords(
      String coin, String startTime, String endTime, String limit) throws IOException {
    checkAuthenticated();
    return classifyingExceptions(
        () ->
            decorateApiCall(
                    () ->
                        accountAPI.getDepositRecords(
                            apiKey,
                            digest,
                            passphrase,
                            nonceFactory,
                            coin,
                            startTime,
                            endTime,
                            limit))
                .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }
}
