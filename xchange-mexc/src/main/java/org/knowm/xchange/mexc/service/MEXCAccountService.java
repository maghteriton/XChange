package org.knowm.xchange.mexc.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.AddressWithTag;
import org.knowm.xchange.dto.account.DepositAddress;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.meta.CurrencyChainStatus;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.mexc.MEXCAdapters;
import org.knowm.xchange.mexc.dto.MEXCResult;
import org.knowm.xchange.mexc.dto.account.MEXCBalance;
import org.knowm.xchange.mexc.dto.market.MEXCCurrency;
import org.knowm.xchange.mexc.dto.market.MEXCCurrencyInfo;
import org.knowm.xchange.mexc.v3.dto.CoinInfo;
import org.knowm.xchange.mexc.v3.dto.Network;
import org.knowm.xchange.mexc.v3.service.MEXCAccountServiceV3;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.DefaultWithdrawFundsParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;

public class MEXCAccountService extends MEXCAccountServiceRaw implements AccountService {

  private Exchange exchangeV3;

  public MEXCAccountService(Exchange exchange) {
    super(exchange);
  }

  public MEXCAccountService(Exchange exchange, Exchange exchangeV3) {
    super(exchange);
    this.exchangeV3 = exchangeV3;
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

  @Override
  public CurrencyChainStatus getCurrencyChainStatus(Currency currency, String chain)
      throws IOException {
    List<MEXCCurrencyInfo> coinList = getCurrencyList(currency.getCurrencyCode());
    // contract address query only supported by v3
    CoinInfo coinInfo = ((MEXCAccountServiceV3) exchangeV3.getAccountService()).getCoin(currency);
    String contractAddress = null;
    for (Network network : coinInfo.getNetworkList()) {
      if (network.getNetwork().equalsIgnoreCase(chain)) {
        contractAddress = network.getContract();
      }
    }

    for (MEXCCurrencyInfo mexcCurrencyInfo : coinList) {
      for (MEXCCurrency mexcCurrency : mexcCurrencyInfo.getMexcCoinList()) {
        if (mexcCurrency.getChain().equalsIgnoreCase(chain)) {

          return new CurrencyChainStatus(
              currency,
              mexcCurrency.getChain(),
              contractAddress,
              mexcCurrency.getIsDepositEnabled(),
              mexcCurrency.getIsWithdrawEnabled(),
              mexcCurrency.getWithdrawalFee(),
              mexcCurrency.getWithdrawalFee());
        }
      }
    }

    return null; // returns null if not found
  }

  @Override
  public Map<Instrument, Boolean> getSupportedInstruments(Instrument... instruments) throws IOException {
    return ((MEXCAccountServiceV3) exchangeV3.getAccountService()).getSymbols();
  }
}
