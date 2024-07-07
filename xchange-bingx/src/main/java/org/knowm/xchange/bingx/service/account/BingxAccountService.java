package org.knowm.xchange.bingx.service.account;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.knowm.xchange.bingx.BingxAdapter;
import org.knowm.xchange.bingx.BingxExchange;
import org.knowm.xchange.bingx.dto.*;
import org.knowm.xchange.bingx.dto.wrapper.BingxBalancesWrapper;
import org.knowm.xchange.bingx.dto.wrapper.BingxWithdrawWrapper;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.DepositAddress;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.meta.CurrencyChainStatus;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.*;

public class BingxAccountService extends BingxAccountServiceRaw implements AccountService {

  public static final String FUNDING_WALLET = "1";
  private Map<Instrument, Boolean> supportedInstrumentMap;

  public BingxAccountService(BingxExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {
    BingxBalancesWrapper balances = getBalances();
    return new AccountInfo(BingxAdapter.adaptWallet(balances.getBalances()));
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

    BingxWithdrawWrapper withdraw =
        withdraw(
            defaultParams.getAddress(),
            defaultParams.getAddressTag(),
            defaultParams.getAmount(),
            defaultParams.getCurrency().getCurrencyCode(),
            defaultParams.getChain(),
            FUNDING_WALLET);

    return withdraw.getId();
  }

  @Override
  public List<DepositAddress> getDepositAddresses(Currency currency) throws IOException {
    List<DepositAddress> depositAddresses =
        BingxAdapter.adaptDepositAddresses(
            getDepositAddresses(currency.getCurrencyCode()).getData());
    // if address is not created, just return network information
    if (depositAddresses.isEmpty()) {
      List<BingxWalletDTO> wallets = getWallets(currency.getCurrencyCode());
      List<BingxNetworkDTO> networkList = wallets.get(0).getNetworkList();
      networkList.forEach(
          network ->
              depositAddresses.add(
                  new DepositAddress(
                      currency.getCurrencyCode(), null, null, network.getNetwork())));
    }
    return depositAddresses;
  }

  @Override
  public CurrencyChainStatus getCurrencyChainStatus(Currency currency, String chain) {
    List<BingxWalletDTO> wallets = getWallets(null);

    if (wallets != null && !wallets.isEmpty()) {
      for (BingxWalletDTO wallet : wallets) {
        if (wallet.getCoin().equalsIgnoreCase(currency.getCurrencyCode())) {
          List<BingxNetworkDTO> networkList = wallet.getNetworkList();
          if (!networkList.isEmpty()) {
            for (BingxNetworkDTO bingxNetwork : networkList) {
              if (bingxNetwork.getNetwork().equalsIgnoreCase(chain)) {
                return new CurrencyChainStatus(
                    currency,
                    bingxNetwork.getNetwork(),
                    bingxNetwork.getContractAddress(),
                    bingxNetwork.isDepositEnable(),
                    bingxNetwork.isWithdrawEnable(),
                    bingxNetwork.getWithdrawFee(),
                    bingxNetwork.getWithdrawFee());
              }
            }
          }
        }
      }
    }
    return null; // returns null if not found
  }

  @Override
  public Map<Instrument, Boolean> getSupportedInstruments(Instrument... instruments)
      throws IOException {
    return supportedInstrumentMap;
  }

  public void setSupportedInstruments(Map<Instrument, Boolean> supportedInstruments) {
    this.supportedInstrumentMap = supportedInstruments;
  }
}
