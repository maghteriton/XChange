package org.knowm.xchange.bingx.service.account;

import static org.knowm.xchange.bingx.BingxExceptionClassifier.classifyingExceptions;
import static org.knowm.xchange.bingx.BingxResilience.PRIVATE_REST_ENDPOINT_RATE_LIMITER;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.knowm.xchange.bingx.BingxExchange;
import org.knowm.xchange.bingx.dto.*;
import org.knowm.xchange.bingx.dto.wrapper.BingxBalancesWrapper;
import org.knowm.xchange.bingx.dto.wrapper.BingxWithdrawWrapper;
import org.knowm.xchange.bingx.service.BingxBaseService;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.exceptions.ExchangeException;

public class BingxAccountServiceRaw extends BingxBaseService {

  private static final String DEFAULT_CACHE_KEY = "defaultKey";
  private final Cache<String, List<BingxWalletDTO>> coinsCache;
  private final Cache<String, List<BingxDepositAddressesDTO>> depositAddrCache;

  public BingxAccountServiceRaw(BingxExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);

    this.coinsCache = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.MINUTES).build();
    this.depositAddrCache = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.MINUTES).build();
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

  public List<BingxWalletDTO> getWallets(String coin) {
    String cacheKey = coin == null ? DEFAULT_CACHE_KEY : coin;
    try {
      return coinsCache.get(
          cacheKey,
          () ->
              classifyingExceptions(
                  () ->
                      decorateApiCall(
                              () ->
                                  accountAPI.getWallets(
                                      apiKey, nonceFactory, 5000, signatureCreator, coin))
                          .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                          .call()));
    } catch (ExecutionException e) {
      throw new ExchangeException(e);
    }
  }

  public List<BingxDepositAddressesDTO> getDepositAddresses(String coin) throws IOException {
    String cacheKey = coin == null ? DEFAULT_CACHE_KEY : coin;
    try {
      return depositAddrCache.get(
          cacheKey,
          () ->
              classifyingExceptions(
                      () ->
                          decorateApiCall(
                                  () ->
                                      accountAPI.getDepositAddresses(
                                          apiKey, nonceFactory, 5000, signatureCreator, coin))
                              .withRateLimiter(rateLimiter(PRIVATE_REST_ENDPOINT_RATE_LIMITER))
                              .call())
                  .getData());
    } catch (ExecutionException e) {
      throw new ExchangeException(e);
    }
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
