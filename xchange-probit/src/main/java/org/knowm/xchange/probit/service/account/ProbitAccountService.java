package org.knowm.xchange.probit.service.account;

import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.DepositAddress;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.meta.CurrencyChainStatus;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.probit.ProbitAdapter;
import org.knowm.xchange.probit.ProbitException;
import org.knowm.xchange.probit.ProbitExchange;
import org.knowm.xchange.probit.dto.request.ProbitWithdrawalRequestDTO;
import org.knowm.xchange.probit.dto.response.*;
import org.knowm.xchange.probit.model.ProbitTransferType;
import org.knowm.xchange.probit.service.market.ProbitMarketDataService;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProbitAccountService extends ProbitAccountServiceRaw implements AccountService {

  private Map<Instrument, Boolean> supportedInstrumentMap;
  private final Map<Currency, String> currencyPlatformMap;

  private static final String CONTRACT_ADDR_NOT_SUPPORTED_BY_API = "notSupportedByAPI";

  public ProbitAccountService(ProbitExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
    try {
      currencyPlatformMap =
          ProbitAdapter.getCurrencyPlatforms(
              ((ProbitMarketDataService) exchange.getMarketDataService()).getCurrency());
    } catch (IOException e) {
      throw new ExchangeException("Could not init ProbitAccountService", e);
    }
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {
    ProbitResultDTO<List<ProbitBalanceDTO>> balances = getBalances();
    return new AccountInfo(ProbitAdapter.adaptWallet(balances.getData()));
  }

  @Override
  public List<FundingRecord> getFundingHistory(TradeHistoryParams params) throws IOException {
    Currency currency = new Currency("");
    Date startTime = null;
    Date endTime = null;
    ProbitTransferType transferType = null;

    if (params instanceof TradeHistoryParamsAll) {
      TradeHistoryParamsAll tradeHistoryParamCurrencyPair = (TradeHistoryParamsAll) params;
      currency = tradeHistoryParamCurrencyPair.getCurrencyPair().getBase();
      startTime = tradeHistoryParamCurrencyPair.getStartTime();
      endTime = tradeHistoryParamCurrencyPair.getEndTime();
      transferType = ProbitAdapter.adaptTransferType(tradeHistoryParamCurrencyPair.getType());
    }

    List<ProbitTransferDTO> data;
    try {
      data = getTransfers(currency, transferType, null, startTime, endTime, 20).getData();
    } catch (ProbitException e) {
      throw new ExchangeException(
          String.format(
              "Code: %s, Message: %s, Details: %s",
              e.getErrorCode(), e.getMessage(), e.getDetails()));
    }
    return ProbitAdapter.adaptTransferHistory(data);
  }

  @Override
  public String withdrawFunds(WithdrawFundsParams params) throws IOException {
    if (!(params instanceof DefaultWithdrawFundsParams)) {
      throw new IllegalArgumentException("DefaultWithdrawFundsParams must be provided.");
    }

    DefaultWithdrawFundsParams defaultParams = (DefaultWithdrawFundsParams) params;

    ProbitWithdrawalRequestDTO probitWithdrawalRequestDTO =
        ProbitWithdrawalRequestDTO.builder()
            .currency_id(defaultParams.getCurrency().getCurrencyCode())
            .platform_id(defaultParams.getChain())
            .address(defaultParams.getAddress())
            .destination_tag(defaultParams.getAddressTag())
            .amount(defaultParams.getAmount().toPlainString())
            .fee_currency_id(ProbitAdapter.getPlatformCurrency(defaultParams.getChain()))
            .build();

    ProbitWithdrawalDTO withdraw;
    try {
      withdraw = withdrawal(probitWithdrawalRequestDTO).getData();
    } catch (ProbitException e) {
      throw new ExchangeException(
          String.format(
              "Code: %s, Message: %s, Details: %s",
              e.getErrorCode(), e.getMessage(), e.getDetails()));
    }

    return withdraw.getId();
  }

  @Override
  public List<DepositAddress> getDepositAddresses(Currency currency) throws IOException {
    List<DepositAddress> depositAddressesWithoutNetwork;
    try {
      depositAddressesWithoutNetwork =
          ProbitAdapter.adaptDepositAddresses(getPlatformDepositAddress(currency, null).getData());
    } catch (ProbitException e) {
      throw new ExchangeException(
          String.format(
              "Code: %s, Message: %s, Details: %s",
              e.getErrorCode(), e.getMessage(), e.getDetails()));
    }

    if (depositAddressesWithoutNetwork.size() == 1) {
      DepositAddress depositAddress = depositAddressesWithoutNetwork.get(0);
      String platform = currencyPlatformMap.get(currency);
      return List.of(
          new DepositAddress(
              depositAddress.getCurrency(),
              depositAddress.getAddress(),
              depositAddress.getAddressTag(),
              platform));
    } else {
      throw new ExchangeException("Multiple Probit deposit address value returned!");
    }
  }

  @Override
  public CurrencyChainStatus getCurrencyChainStatus(Currency currency, String chain)
      throws IOException {
    List<ProbitCurrencyDTO> currencyList;
    try {
      currencyList = ((ProbitMarketDataService) exchange.getMarketDataService()).getCurrency();
    } catch (ProbitException e) {
      throw new ExchangeException(
          String.format(
              "Code: %s, Message: %s, Details: %s",
              e.getErrorCode(), e.getMessage(), e.getDetails()));
    }

    if (!currencyList.isEmpty()) {
      for (ProbitCurrencyDTO probitCurrencyDTO : currencyList) {
        if (probitCurrencyDTO.getId().equals(currency.getCurrencyCode())) {
          if (probitCurrencyDTO.getPlatform().equalsIgnoreCase(chain)) {

            ProbitWithdrawalFeeDTO platformWithdrawalFee =
                ProbitAdapter.getPlatformWithdrawalFee(probitCurrencyDTO);

            if (platformWithdrawalFee != null) {
              return new CurrencyChainStatus(
                  currency,
                  probitCurrencyDTO.getPlatform(),
                  CONTRACT_ADDR_NOT_SUPPORTED_BY_API,
                  !probitCurrencyDTO.getDepositSuspended(),
                  !probitCurrencyDTO.getWithdrawalSuspended(),
                  new Currency(platformWithdrawalFee.getCurrencyId()),
                  platformWithdrawalFee.getAmount(),
                  platformWithdrawalFee.getAmount());
            }
          }
        }
      }
    }
    return null; // returns null if not found
  }

  @Override
  public Map<Instrument, Boolean> getSupportedInstruments(Instrument... instruments) {
    return supportedInstrumentMap;
  }

  public void setSupportedInstruments(Map<Instrument, Boolean> supportedInstruments) {
    this.supportedInstrumentMap = supportedInstruments;
  }
}
