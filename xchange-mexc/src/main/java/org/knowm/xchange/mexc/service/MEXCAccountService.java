package org.knowm.xchange.mexc.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.AddressWithTag;
import org.knowm.xchange.dto.account.DepositAddress;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.mexc.MEXCAdapters;
import org.knowm.xchange.mexc.dto.MEXCResult;
import org.knowm.xchange.mexc.dto.account.MEXCBalance;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.DefaultWithdrawFundsParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;

public class MEXCAccountService extends MEXCAccountServiceRaw implements AccountService {

  public MEXCAccountService(Exchange exchange) {
    super(exchange);
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {
    try {
      MEXCResult<Map<String, MEXCBalance>> walletBalances = getWalletBalances();
      Map<String, MEXCBalance> walletBalancesResult = walletBalances.getData();
      return new AccountInfo(MEXCAdapters.adaptMEXCBalances(walletBalancesResult));
    } catch (MEXCException e) {
      throw new ExchangeException(e);
    }
  }

  @Override
  public String withdrawFunds(WithdrawFundsParams params) throws IOException {
    if (params instanceof DefaultWithdrawFundsParams) {
      DefaultWithdrawFundsParams defaultParams = (DefaultWithdrawFundsParams) params;
      AddressWithTag addressWithTag =
          new AddressWithTag(defaultParams.getAddress(), defaultParams.getAddressTag());
      String withdrawId;
      try {
        withdrawId =
            createMEXCWithdraw(
                    defaultParams.getCurrency(),
                    defaultParams.getChain(),
                    defaultParams.getAmount(),
                    addressWithTag)
                .getWithdrawId();
      } catch (MEXCException e) {
        throw new ExchangeException(e);
      }
      return withdrawId;
    }
    throw new IllegalStateException("Don't know how to withdraw: " + params);
  }

  @Override
  public List<FundingRecord> getFundingHistory(TradeHistoryParams params) throws IOException {
    List<FundingRecord> fundingRecords;
    try {
      fundingRecords =
          MEXCAdapters.adaptDepositWithdrawLists(getMEXCDeposits(), getMEXCWithdrawals());
    } catch (MEXCException e) {
      throw new ExchangeException(e);
    }
    return fundingRecords;
  }

  @Override
  public List<DepositAddress> getDepositAddresses(Currency currency) throws IOException {
    List<DepositAddress> depositAddresses;
    try {
      depositAddresses = MEXCAdapters.adaptDepositAddresses(getMEXCDepositAddress(currency));
    } catch (MEXCException e) {
      throw new ExchangeException(e);
    }
    return depositAddresses;
  }
}
