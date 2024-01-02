package org.knowm.xchange.bingx.service.account;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.bingx.BingxException;
import org.knowm.xchange.bingx.dto.*;
import org.knowm.xchange.bingx.dto.wrapper.BingxBalancesWrapper;
import org.knowm.xchange.bingx.dto.wrapper.BingxDepositAddressesWrapper;
import org.knowm.xchange.bingx.dto.wrapper.BingxWithdrawWrapper;
import org.knowm.xchange.bingx.service.BingxBaseService;
import org.knowm.xchange.exceptions.ExchangeException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class BingxAccountServiceRaw extends BingxBaseService {
  public BingxAccountServiceRaw(Exchange exchange) {
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

  public List<BingxWalletDTO> getWallets(String coin) throws IOException {
    BingxResultDTO<List<BingxWalletDTO>> wallets;
    try {
      wallets = accountAPI.getWallets(apiKey, nonceFactory, 5000, signatureCreator, coin);
    } catch (BingxException e) {
      throw new ExchangeException(e);
    }
    return wallets.getData();
  }

  public List<BingxDepositAddressesDTO> getDepositAddresses(String coin) throws IOException {
    BingxResultDTO<BingxDepositAddressesWrapper> depositAddresses;
    try {
      depositAddresses =
          accountAPI.getDepositAddresses(apiKey, nonceFactory, 5000, signatureCreator, coin);
    } catch (BingxException e) {
      throw new ExchangeException(e);
    }
    return depositAddresses.getData().getData();
  }

  public BingxWithdrawWrapper withdraw(
      String address, String tag, BigDecimal amount, String coin, String network, String walletType)
      throws IOException {
    BingxResultDTO<BingxWithdrawWrapper> withdraw;
    try {
      withdraw =
          accountAPI.withdraw(
              apiKey,
              nonceFactory,
              5000,
              signatureCreator,
              address,
              tag,
              amount,
              coin,
              network,
              walletType);
    } catch (BingxException e) {
      throw new ExchangeException(e);
    }
    return withdraw.getData();
  }
}
