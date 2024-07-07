package org.knowm.xchange.bitget.service;

import org.knowm.xchange.bitget.BitgetAdapter;
import org.knowm.xchange.bitget.BitgetExchange;
import org.knowm.xchange.bitget.model.dto.response.BitgetCoinsResponse;
import org.knowm.xchange.bitget.model.dto.response.BitgetOrderIdResponse;
import org.knowm.xchange.bitget.service.exception.BitgetApiException;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AddressWithTag;
import org.knowm.xchange.dto.account.DepositAddress;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.meta.CurrencyChainStatus;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class BitgetAccountService extends BitgetAccountServiceRaw implements AccountService {
  private List<BitgetCoinsResponse> bitgetCoin;
  private BitgetMarketDataServiceRaw bitgetMarketDataServiceRaw;

  public BitgetAccountService(
      BitgetExchange bitgetExchange, ResilienceRegistries resilienceRegistries) {
    super(bitgetExchange, resilienceRegistries);
  }

  @Override
  public List<DepositAddress> getDepositAddresses(Currency currency) {
    List<DepositAddress> depositAddresses = new ArrayList<>();
    List<BitgetCoinsResponse.Chain> chains =
        bitgetCoin.stream()
            .filter(bitgetCoin -> currency.getCurrencyCode().equalsIgnoreCase(bitgetCoin.getCoin()))
            .flatMap(coin -> coin.getChains().stream())
            .collect(Collectors.toList());

    chains.forEach(
        c -> {
          try {
            if (c.getRechargeable()) {
              depositAddresses.add(
                  BitgetAdapter.adaptBitgetDeposit(
                      getDepositAddress(currency.getCurrencyCode(), c.getChain())));
            }
          } catch (BitgetApiException | IOException e) {
            throw new ExchangeException(e);
          }
        });

    return depositAddresses;
  }

  @Override
  public DepositAddress getDepositAddress(Currency currency, String chain) throws IOException {
    return BitgetAdapter.adaptBitgetDeposit(getDepositAddress(currency.getCurrencyCode(), chain));
  }

  @Override
  public String withdrawFunds(WithdrawFundsParams params) throws IOException {
    if (params instanceof DefaultWithdrawFundsParams) {
      DefaultWithdrawFundsParams defaultParams = (DefaultWithdrawFundsParams) params;
      AddressWithTag addressWithTag =
          new AddressWithTag(defaultParams.getAddress(), defaultParams.getAddressTag());

      String orderId = null;
      try {

        BitgetOrderIdResponse withdraw =
            withdraw(
                defaultParams.getCurrency().getCurrencyCode(),
                defaultParams.getChain(),
                defaultParams.getAmount(),
                addressWithTag);

        if (withdraw != null) {
          orderId = withdraw.getOrderId();
        }

      } catch (BitgetApiException e) {
        throw new ExchangeException(e);
      }
      return orderId;
    }
    throw new IllegalStateException("Don't know how to withdraw: " + params);
  }

  @Override
  public List<FundingRecord> getFundingHistory(TradeHistoryParams params) throws IOException {

    if (params instanceof TradeHistoryParamsAll) {
      TradeHistoryParamsAll tradeHistoryParamCurrencyPair = (TradeHistoryParamsAll) params;
      Currency currency = tradeHistoryParamCurrencyPair.getCurrencyPair().getBase();
      long startTime = tradeHistoryParamCurrencyPair.getStartTime().getTime();
      long endTime = tradeHistoryParamCurrencyPair.getEndTime().getTime();
      Integer limit = tradeHistoryParamCurrencyPair.getLimit();

      return BitgetAdapter.adaptFundingHistory(
          getDepositRecords(
              currency.getCurrencyCode(),
              String.valueOf(startTime),
              String.valueOf(endTime),
              String.valueOf(limit)));
    }
    throw new IllegalStateException("Don't know how to get funding history: " + params);
  }

  @Override
  public CurrencyChainStatus getCurrencyChainStatus(Currency currency, String chain) {

    List<BitgetCoinsResponse> bitgetCoins = null;
    if (marketAPI != null) {
      bitgetCoins = bitgetMarketDataServiceRaw.getBitgetCoins(null);
    }

    if (bitgetCoins != null && !bitgetCoins.isEmpty()) {
      for (BitgetCoinsResponse bitgetCoinsResponse : bitgetCoins) {
        if (bitgetCoinsResponse.getCoin().equalsIgnoreCase(currency.getCurrencyCode())) {
          List<BitgetCoinsResponse.Chain> chains = bitgetCoinsResponse.getChains();
          for (BitgetCoinsResponse.Chain bitgetChain : chains) {
            if (bitgetChain.getChain().equalsIgnoreCase(chain)) {
              return new CurrencyChainStatus(
                  currency,
                  bitgetChain.getChain(),
                  bitgetChain.getContractAddress(),
                  bitgetChain.getRechargeable(),
                  bitgetChain.getWithdrawable(),
                  bitgetChain.getWithdrawFee(),
                  bitgetChain.getWithdrawFee());
            }
          }
        }
      }
    }
    return null; // returns null if not found
  }

  public void setBitgetCoinInformation(List<BitgetCoinsResponse> bitgetCoins) {
    this.bitgetCoin = bitgetCoins;
  }

  public void setMarketDataServiceRaw(BitgetMarketDataServiceRaw marketDataService) {
    this.bitgetMarketDataServiceRaw = marketDataService;
  }
}
