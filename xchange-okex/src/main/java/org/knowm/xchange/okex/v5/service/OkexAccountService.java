package org.knowm.xchange.okex.v5.service;

import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.DepositAddress;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.okex.v5.OkexAdapters;
import org.knowm.xchange.okex.v5.OkexExchange;
import org.knowm.xchange.okex.v5.dto.OkexResponse;
import org.knowm.xchange.okex.v5.dto.account.*;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.*;

import java.io.IOException;
import java.util.List;

/**
 * Author: Max Gao (gaamox@tutanota.com) Created: 08-06-2021
 */
public class OkexAccountService extends OkexAccountServiceRaw implements AccountService {

    private static final String COLON = ":";

    public OkexAccountService(OkexExchange exchange, ResilienceRegistries resilienceRegistries) {
        super(exchange, resilienceRegistries);
    }

    public AccountInfo getAccountInfo() throws IOException {
        // null to get assets (with non-zero balance), remaining balance, and available amount in the
        // account.
        OkexResponse<List<OkexWalletBalance>> tradingBalances = getWalletBalances(null);
        OkexResponse<List<OkexAssetBalance>> assetBalances = getAssetBalances(null);
        return new AccountInfo(
                OkexAdapters.adaptOkexBalances(tradingBalances.getData()),
                OkexAdapters.adaptOkexAssetBalances(assetBalances.getData()));
    }


    @Override
    public String withdrawFunds(WithdrawFundsParams params) throws IOException {
        DefaultWithdrawFundsParams defaultParams;
        if (params instanceof DefaultWithdrawFundsParams) {
            defaultParams = (DefaultWithdrawFundsParams) params;
        } else {
            throw new IllegalStateException("DefaultWithdrawFundsParams must be provided.");
        }

        String addressFromParams = defaultParams.getAddress();
        String addressTagFromParams = defaultParams.getAddressTag();
        String address = addressTagFromParams != null ? addressFromParams + COLON + addressTagFromParams : addressFromParams;
        OkexWithdrawRequest okexWithdrawRequest = OkexWithdrawRequest.builder()
                .currency(defaultParams.getCurrency().getCurrencyCode())
                .withdrawalAmount(defaultParams.getAmount().toString())
                .destination("4")
                .toAddress(address)
                .transactionFee(defaultParams.getCommission().toString())
                .chain(defaultParams.getChain())
                .build();
        OkexResponse<List<WithdrawalInfo>> withdraw = withdraw(okexWithdrawRequest);

        return withdraw.getData().get(0).getWithdrawalId();
    }

  @Override
  public List<FundingRecord> getFundingHistory(TradeHistoryParams params) throws IOException {
    String currency = null;
    if (params instanceof TradeHistoryParamCurrency
        && ((TradeHistoryParamCurrency) params).getCurrency() != null) {
      currency = ((TradeHistoryParamCurrency) params).getCurrency().getCurrencyCode();
    }

    String start = null;
    String end = null;
    if (params instanceof TradeHistoryParamsTimeSpan) {
      TradeHistoryParamsTimeSpan timeSpanParam = (TradeHistoryParamsTimeSpan) params;
      start =
          String.valueOf(
              timeSpanParam.getStartTime() != null ? timeSpanParam.getStartTime().getTime() : null);

      end =
          String.valueOf(
              timeSpanParam.getEndTime() != null ? timeSpanParam.getEndTime().getTime() : null);
    }

    String limit = null;
    if (params instanceof TradeHistoryParamLimit) {
      limit = String.valueOf(((TradeHistoryParamLimit) params).getLimit());
    }

    OkexDepositHistoryRequest okexDepositHistoryRequest =
        OkexDepositHistoryRequest.builder()
            .currency(currency)
            .after(end)
            .before(start)
            .limit(limit)
            .build();

    return OkexAdapters.adaptOkexDepositHistory(
        getDepositHistory(okexDepositHistoryRequest).getData());
  }

  @Override
  public List<DepositAddress> getDepositAddresses(Currency currency) throws IOException {
    OkexResponse<List<OkexDepositAddress>> okexDepositAddressResponse =
        getDepositAddress(currency.getCurrencyCode());
    return OkexAdapters.adaptOkexDepositAddresses(okexDepositAddressResponse.getData());
  }
}
