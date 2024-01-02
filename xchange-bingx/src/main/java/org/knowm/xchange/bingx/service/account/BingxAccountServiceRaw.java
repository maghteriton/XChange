package org.knowm.xchange.bingx.service.account;

import static org.knowm.xchange.bingx.BingxExceptionClassifier.classifyingExceptions;
import static org.knowm.xchange.bingx.BingxResilience.PRIVATE_REST_ENDPOINT_RATE_LIMITER;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import org.knowm.xchange.bingx.BingxExchange;
import org.knowm.xchange.bingx.dto.*;
import org.knowm.xchange.bingx.dto.wrapper.BingxBalancesWrapper;
import org.knowm.xchange.bingx.dto.wrapper.BingxDepositAddressesWrapper;
import org.knowm.xchange.bingx.dto.wrapper.BingxWithdrawWrapper;
import org.knowm.xchange.bingx.service.BingxBaseService;
import org.knowm.xchange.client.ResilienceRegistries;

public class BingxAccountServiceRaw extends BingxBaseService {
  public BingxAccountServiceRaw(BingxExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  public BingxBalancesWrapper getBalances() throws IOException {
    return classifyingExceptions(
        () ->
            decorateApiCall(
                    () -> accountAPI.getBalances(apiKey, nonceFactory, 5000, signatureCreator))
                .withRetry(retry("getBalances"))
                .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }

  public List<BingxDepositDTO> getDepositHistory(String coin) throws IOException {
    return decorateApiCall(
            () -> accountAPI.getDepositHistory(apiKey, nonceFactory, 5000, signatureCreator, coin))
        .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
        .call();
  }

  public List<BingxWalletDTO> getWallets(String coin) throws IOException {
    return classifyingExceptions(
        () ->
            decorateApiCall(
                    () -> accountAPI.getWallets(apiKey, nonceFactory, 5000, signatureCreator, coin))
                .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }

  public BingxDepositAddressesWrapper getDepositAddresses(String coin) throws IOException {
    return classifyingExceptions(
        () ->
            decorateApiCall(
                    () ->
                        accountAPI.getDepositAddresses(
                            apiKey, nonceFactory, 5000, signatureCreator, coin))
                .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }

  public BingxWithdrawWrapper withdraw(
      String address, String tag, BigDecimal amount, String coin, String network, String walletType)
      throws IOException {
    return classifyingExceptions(
        () ->
            decorateApiCall(
                    () ->
                        accountAPI.withdraw(
                            apiKey,
                            nonceFactory,
                            5000,
                            signatureCreator,
                            address,
                            tag,
                            amount,
                            coin,
                            network,
                            walletType))
                .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                .call());
  }
}
