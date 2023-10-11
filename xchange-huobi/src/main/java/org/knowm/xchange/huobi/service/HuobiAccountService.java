package org.knowm.xchange.huobi.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.AddressWithTag;
import org.knowm.xchange.dto.account.DepositAddress;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.meta.CurrencyChainStatus;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.huobi.HuobiAdapters;
import org.knowm.xchange.huobi.HuobiUtils;
import org.knowm.xchange.huobi.dto.account.HuobiAccount;
import org.knowm.xchange.huobi.dto.account.HuobiDepositAddress;
import org.knowm.xchange.huobi.dto.marketdata.HuobiChain;
import org.knowm.xchange.huobi.dto.marketdata.HuobiCurrency;
import org.knowm.xchange.huobi.dto.marketdata.HuobiCurrencyWrapper;
import org.knowm.xchange.huobi.dto.marketdata.results.HuobiCurrenciesResult;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.*;

public class HuobiAccountService extends HuobiAccountServiceRaw implements AccountService {

  public HuobiAccountService(Exchange exchange) {
    super(exchange);
  }

  @Override
  public String withdrawFunds(WithdrawFundsParams params) throws IOException {
    if (params instanceof DefaultWithdrawFundsParams) {
      DefaultWithdrawFundsParams defaultParams = (DefaultWithdrawFundsParams) params;
      return String.valueOf(
          createWithdraw(
              defaultParams.getCurrency().getCurrencyCode(),
              defaultParams.getAmount(),
              defaultParams.getCommission(),
              defaultParams.getAddress(),
              defaultParams.getAddressTag(),
              defaultParams.getChain()));
    }
    throw new IllegalStateException("Don't know how to withdraw: " + params);
  }

  @Override
  public String withdrawFunds(Currency currency, BigDecimal amount, AddressWithTag address)
      throws IOException {
    return String.valueOf(
        createWithdraw(
            currency.toString(),
            amount,
            null,
            address.getAddress(),
            address.getAddressTag(),
            null));
  }

  @Override
  public String withdrawFunds(Currency currency, BigDecimal amount, String address)
      throws IOException {
    return String.valueOf(createWithdraw(currency.toString(), amount, null, address, null, null));
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {
    HuobiAccount[] accounts = getAccounts();
    if (accounts.length == 0) {
      throw new ExchangeException("Account is not recognized.");
    }
    String accountID = String.valueOf(accounts[0].getId());
    return new AccountInfo(
        accountID,
        HuobiAdapters.adaptWallet(
            HuobiAdapters.adaptBalance(getHuobiBalance(accountID).getList())));
  }

  @Override
  public TradeHistoryParams createFundingHistoryParams() {
    return new HuobiFundingHistoryParams(null, null, null);
  }

  @Override
  public List<FundingRecord> getFundingHistory(TradeHistoryParams params) throws IOException {
    String currency = null;
    if (params instanceof TradeHistoryParamCurrency
        && ((TradeHistoryParamCurrency) params).getCurrency() != null) {
      currency = ((TradeHistoryParamCurrency) params).getCurrency().getCurrencyCode();
    }

    String from = null;
    if (params instanceof TradeHistoryParamsIdSpan) {
      from = ((TradeHistoryParamsIdSpan) params).getStartId();
    }

    FundingRecord.Type type;
    if (params instanceof HistoryParamsFundingType
        && ((HistoryParamsFundingType) params).getType() != null) {
      type = ((HistoryParamsFundingType) params).getType();
    } else {
      // Funding history type is a required parameter for Huobi funding history query
      throw new ExchangeException(
          "Type 'deposit' or 'withdraw' must be supplied using FundingRecord.Type");
    }

    TradeHistoryParamsSorted.Order order = null;
    if (params instanceof TradeHistoryParamsSorted
        && ((TradeHistoryParamsSorted) params).getOrder() != null) {
      order = ((TradeHistoryParamsSorted) params).getOrder();
    }

    // Adapt type out (replace withdrawal -> withdraw)
    String fundingRecordType = type == FundingRecord.Type.WITHDRAWAL ? "withdraw" : "deposit";
    // Adapt direct out (replace asc -> prev 'default', desc -> next)
    String sortingStyle = order == TradeHistoryParamsSorted.Order.desc ? "next" : "prev";
    return HuobiAdapters.adaptFundingHistory(
        getDepositWithdrawalHistory(currency, fundingRecordType, from, sortingStyle));
  }

  @Override
  public String requestDepositAddress(Currency currency, String... strings) throws IOException {
    return getDepositAddress(currency.toString());
  }

  @Override
  public AddressWithTag requestDepositAddressData(Currency currency, String... args)
      throws IOException {
    HuobiDepositAddress huobiAddrWithTag = getDepositAddressV2(currency.toString())[0];
    return new AddressWithTag(huobiAddrWithTag.getAddress(), huobiAddrWithTag.getAddressTag());
  }

  @Override
  public List<DepositAddress> getDepositAddresses(Currency currency) throws IOException {
    HuobiDepositAddress[] depositAddressV2 = getDepositAddressV2(currency.getCurrencyCode());
    return HuobiAdapters.adaptDepositAddresses(depositAddressV2);
  }

  @Override
  public CurrencyChainStatus getCurrencyChainStatus(Currency currency, String chain)
      throws IOException {

    HuobiChain[] huobiChains = getHuobiChains(currency.getCurrencyCode());
    HuobiCurrencyWrapper huobiCurrencyWrapper = getHuobiCurrencies(currency.getCurrencyCode())[0];

    for (HuobiChain huobiChain : huobiChains) {
      // then get compare by internal huobi chain names.
      if (huobiChain.getDisplayName().equalsIgnoreCase(chain)) {

        for (HuobiCurrency huobiCurrency : huobiCurrencyWrapper.getHuobiCurrencies()) {
          if (huobiCurrency.getDisplayName().equalsIgnoreCase(chain)) {
            return new CurrencyChainStatus(
                currency,
                huobiChain.getChain(),
                huobiChain.getContractAddress(),
                huobiChain.isDepositEnable(),
                huobiChain.isWithdrawEnable(),
                huobiCurrency.getTransactFeeWithdraw(),
               huobiCurrency.getTransactFeeWithdraw());
          }
        }
      }
    }

    return null; // returns null if not found
  }
}
