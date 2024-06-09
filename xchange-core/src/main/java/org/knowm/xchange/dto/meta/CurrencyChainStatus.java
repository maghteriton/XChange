package org.knowm.xchange.dto.meta;

import org.knowm.xchange.currency.Currency;

import java.math.BigDecimal;

public class CurrencyChainStatus {

  private final Currency currency;
  private final String chain;
  private final String contractAddress;
  private final Boolean isDepositEnabled;
  private final Boolean isWithdrawalEnabled;
  private final Currency withdrawalFeeCurrency;
  private final BigDecimal minWithdrawalFee;
  private final BigDecimal maxWithdrawalFee;

  public CurrencyChainStatus(
      Currency currency,
      String chain,
      String contractAddress,
      Boolean isDepositEnabled,
      Boolean isWithdrawalEnabled,
      Currency withdrawalFeeCurrency,
      BigDecimal minWithdrawalFee,
      BigDecimal maxWithdrawalFee) {
    this.currency = currency;
    this.chain = chain;
    this.contractAddress = contractAddress;
    this.isDepositEnabled = isDepositEnabled;
    this.isWithdrawalEnabled = isWithdrawalEnabled;
    this.withdrawalFeeCurrency = withdrawalFeeCurrency == null ? currency : withdrawalFeeCurrency;
    this.minWithdrawalFee = minWithdrawalFee;
    this.maxWithdrawalFee = maxWithdrawalFee;
  }

  public CurrencyChainStatus(
          Currency currency,
          String chain,
          String contractAddress,
          Boolean isDepositEnabled,
          Boolean isWithdrawalEnabled,
          BigDecimal minWithdrawalFee,
          BigDecimal maxWithdrawalFee) {
    this.currency = currency;
    this.chain = chain;
    this.contractAddress = contractAddress;
    this.isDepositEnabled = isDepositEnabled;
    this.isWithdrawalEnabled = isWithdrawalEnabled;
    this.withdrawalFeeCurrency = currency;
    this.minWithdrawalFee = minWithdrawalFee;
    this.maxWithdrawalFee = maxWithdrawalFee;
  }

  public Currency getCurrency() {
    return currency;
  }

  public String getChain() {
    return chain;
  }

  public String getContractAddress() {
    return contractAddress;
  }

  public Boolean isDepositEnabled() {
    return isDepositEnabled;
  }

  public Boolean isWithdrawalEnabled() {
    return isWithdrawalEnabled;
  }

  public Currency getWithdrawalFeeCurrency() {
    return withdrawalFeeCurrency;
  }

  public BigDecimal getMinWithdrawalFee() {
    return minWithdrawalFee;
  }

  public BigDecimal getMaxWithdrawalFee() {
    return maxWithdrawalFee;
  }
}
