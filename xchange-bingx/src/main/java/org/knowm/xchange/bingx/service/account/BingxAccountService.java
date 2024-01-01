package org.knowm.xchange.bingx.service.account;

import java.io.IOException;
import java.util.List;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.bingx.BingxAdapter;
import org.knowm.xchange.bingx.BingxException;
import org.knowm.xchange.bingx.dto.*;
import org.knowm.xchange.bingx.dto.wrapper.BingxBalancesWrapper;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.DepositAddress;
import org.knowm.xchange.dto.meta.CurrencyChainStatus;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.DefaultWithdrawFundsParams;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;

public class BingxAccountService extends BingxAccountServiceRaw implements AccountService {

  public static final String CONTRACT_ADDRESS_NOT_SUPPORTED = "notSupportedByAPI";

  public BingxAccountService(Exchange exchange) {
    super(exchange);
  }

  public List<BingxBalanceDTO> getBalances() throws IOException {
    BingxResultDTO<BingxBalancesWrapper> symbols;
    try {
      symbols = accountAPI.getBalances(apiKey, nonceFactory, 5000, signatureCreator);
    } catch (BingxException e) {
      throw new ExchangeException(e);
    }
    return symbols.getData().getBalances();
  }

  public List<BingxDepositDTO> getDepositHistory(String coin) throws IOException {
    List<BingxDepositDTO> depositHistory;
    try {
      depositHistory =
          accountAPI.getDepositHistory(apiKey, nonceFactory, 5000, signatureCreator, coin);
    } catch (BingxException e) {
      throw new ExchangeException(e);
    }
    return depositHistory;
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
            "1")
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
