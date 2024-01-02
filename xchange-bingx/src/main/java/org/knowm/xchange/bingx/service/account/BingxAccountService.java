package org.knowm.xchange.bingx.service.account;

import java.io.IOException;
import java.util.List;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.bingx.BingxAdapter;
import org.knowm.xchange.bingx.dto.*;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.DepositAddress;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.meta.CurrencyChainStatus;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.*;

public class BingxAccountService extends BingxAccountServiceRaw implements AccountService {

  public static final String CONTRACT_ADDRESS_NOT_SUPPORTED = "notSupportedByAPI";
  public static final String FUNDING_WALLET = "1";

  public BingxAccountService(Exchange exchange) {
    super(exchange);
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {
    return new AccountInfo(BingxAdapter.adaptWallet(getBalances()));
  }

  @Override
  public List<FundingRecord> getFundingHistory(TradeHistoryParams params) throws IOException {
    Currency currency = new Currency("");

    if (params instanceof TradeHistoryParamCurrencyPair) {
      TradeHistoryParamCurrencyPair tradeHistoryParamCurrencyPair =
          (TradeHistoryParamCurrencyPair) params;
      currency = tradeHistoryParamCurrencyPair.getCurrencyPair().getBase();
    }

    return BingxAdapter.adaptFundingHistory(getDepositHistory(currency.getCurrencyCode()));
  }

  @Override
  public String withdrawFunds(WithdrawFundsParams params) throws IOException {
    if (!(params instanceof DefaultWithdrawFundsParams)) {
      throw new IllegalArgumentException("DefaultWithdrawFundsParams must be provided.");
    }

    DefaultWithdrawFundsParams defaultParams = (DefaultWithdrawFundsParams) params;

    return accountAPI
        .withdraw(
            apiKey,
            nonceFactory,
            5000,
            signatureCreator,
            defaultParams.getAddress(),
            defaultParams.getAddressTag(),
            defaultParams.getAmount(),
            defaultParams.getCurrency().getCurrencyCode(),
            defaultParams.getChain(),
            FUNDING_WALLET)
        .getData()
        .getId();
  }

  @Override
  public List<DepositAddress> getDepositAddresses(Currency currency) throws IOException {
    return BingxAdapter.adaptDepositAddresses(getDepositAddresses(currency.getCurrencyCode()));
  }

  @Override
  public CurrencyChainStatus getCurrencyChainStatus(Currency currency, String chain)
      throws IOException {
    List<BingxWalletDTO> wallets = getWallets(currency.getCurrencyCode());
    List<BingxNetworkDTO> networkList = wallets.get(0).getNetworkList();

    if (!wallets.isEmpty() && !networkList.isEmpty()) {
      for (BingxNetworkDTO bingxNetwork : networkList) {
        if (bingxNetwork.getNetwork().equalsIgnoreCase(chain)) {
          return new CurrencyChainStatus(
              currency,
              bingxNetwork.getNetwork(),
              CONTRACT_ADDRESS_NOT_SUPPORTED,
              bingxNetwork.isDepositEnable(),
              bingxNetwork.isWithdrawEnable(),
              bingxNetwork.getWithdrawFee(),
              bingxNetwork.getWithdrawFee());
        }
      }
    }

    return null; // returns null if not found
  }
}
