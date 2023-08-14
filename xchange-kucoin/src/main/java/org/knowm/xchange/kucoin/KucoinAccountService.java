package org.knowm.xchange.kucoin;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.DepositAddress;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.account.FundingRecord.Type;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.kucoin.dto.request.ApplyWithdrawApiRequest;
import org.knowm.xchange.kucoin.dto.request.InnerTransferRequest;
import org.knowm.xchange.kucoin.dto.response.AccountBalancesResponse;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.*;

public class KucoinAccountService extends KucoinAccountServiceRaw implements AccountService {

  KucoinAccountService(KucoinExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {
    List<AccountBalancesResponse> accounts = getKucoinAccounts();
    return new AccountInfo(
        accounts.stream()
            .map(AccountBalancesResponse::getType)
            .distinct()
            .map(
                type ->
                    Wallet.Builder.from(
                            accounts.stream()
                                .filter(a -> a.getType().equals(type))
                                .map(KucoinAdapters::adaptBalance)
                                .collect(toList()))
                        .id(type)
                        .features(
                            type.equals(KucoinWallet.trade.name())
                                ? Stream.of(Wallet.WalletFeature.TRADING)
                                    .collect(Collectors.toSet())
                                : Stream.of(Wallet.WalletFeature.FUNDING)
                                    .collect(Collectors.toSet()))
                        .build())
            .collect(toList()));
  }

  public TradeHistoryParams createFundingHistoryParams() {
    return new KucoinTradeHistoryParams();
  }

  @Override
  public List<FundingRecord> getFundingHistory(TradeHistoryParams params) throws IOException {
    String currency = null;
    if (params instanceof TradeHistoryParamCurrency) {
      Currency c = ((TradeHistoryParamCurrency) params).getCurrency();
      currency = c == null ? null : c.getCurrencyCode();
    }
    boolean withdrawals = true, deposits = true;
    if (params instanceof HistoryParamsFundingType) {
      HistoryParamsFundingType p = (HistoryParamsFundingType) params;
      withdrawals = p.getType() == null || p.getType() == Type.WITHDRAWAL;
      deposits = p.getType() == null || p.getType() == Type.DEPOSIT;
    }

    Long startAt = null, endAt = null;
    if (params instanceof TradeHistoryParamsTimeSpan) {
      TradeHistoryParamsTimeSpan p = (TradeHistoryParamsTimeSpan) params;
      startAt = p.getStartTime() == null ? null : p.getStartTime().getTime();
      endAt = p.getEndTime() == null ? null : p.getEndTime().getTime();
    }
    List<FundingRecord> result = new ArrayList<>();
    if (withdrawals) {
      result.addAll(
          getWithdrawalsList(currency, null, startAt, endAt, null, null).getItems().stream()
              .map(KucoinAdapters::adaptFundingRecord)
              .collect(Collectors.toList()));
    }
    if (deposits) {
      result.addAll(
          getDepositList(currency, null, startAt, endAt, null, null).getItems().stream()
              .map(KucoinAdapters::adaptFundingRecord)
              .collect(Collectors.toList()));
    }
    return result;
  }

  @Override
  public String withdrawFunds(WithdrawFundsParams params) throws IOException {
    if (!(params instanceof DefaultWithdrawFundsParams)) {
      throw new IllegalArgumentException("DefaultWithdrawFundsParams must be provided.");
    }

    DefaultWithdrawFundsParams defaultParams = (DefaultWithdrawFundsParams) params;

    ApplyWithdrawApiRequest withdrawApiRequest =
        ApplyWithdrawApiRequest.builder()
            .address(defaultParams.getAddress())
            .memo(defaultParams.getAddressTag())
            .currency(defaultParams.getCurrency().getSymbol())
            .amount(defaultParams.amount)
            .build();

    return applyWithdraw(withdrawApiRequest).getWithdrawalId();
  }

  @Override
  public String fundsTransfer(
      Currency currency,
      BigDecimal amount,
      Wallet.WalletFeature fromWalletFeature,
      Wallet.WalletFeature toWalletFeature)
      throws IOException {
    InnerTransferRequest innerTransferRequest =
        InnerTransferRequest.builder()
            .clientOid(String.valueOf(UUID.randomUUID()))
            .currency(currency.getCurrencyCode())
            .from(KucoinAdapters.adaptWalletName(fromWalletFeature))
            .to(KucoinAdapters.adaptWalletName(toWalletFeature))
            .amount(amount)
            .build();
    return innerTransfer(innerTransferRequest).getOrderId();
  }

  @Override
  public List<DepositAddress> getDepositAddresses(Currency currency) throws IOException {
    return KucoinAdapters.adaptDepositAddresses(currency, getDepositAddresses(currency.getCurrencyCode()));
  }
}
