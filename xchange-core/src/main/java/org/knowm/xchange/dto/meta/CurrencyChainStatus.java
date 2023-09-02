package org.knowm.xchange.dto.meta;

import org.knowm.xchange.currency.Currency;

import java.math.BigDecimal;

public class CurrencyChainStatus {

  private final Currency currency;
  private final String chain;
  private final Boolean isDepositEnabled;
  private final Boolean isWithdrawalEnabled;
  private final BigDecimal minWithdrawalFee;
  private final BigDecimal maxWithdrawalFee;

  public CurrencyChainStatus(
      Currency currency,
      String chain,
      Boolean isDepositEnabled,
      Boolean isWithdrawalEnabled,
      BigDecimal minWithdrawalFee,
      BigDecimal maxWithdrawalFee) {
    this.currency = currency;
    this.chain = chain;
    this.isDepositEnabled = isDepositEnabled;
    this.isWithdrawalEnabled = isWithdrawalEnabled;
    this.minWithdrawalFee = minWithdrawalFee;
    this.maxWithdrawalFee = maxWithdrawalFee;
  }

  public Currency getCurrency() {
    return currency;
  }

  public String getChain() {
    return chain;
  }

  public Boolean isDepositEnabled() {
    return isDepositEnabled;
  }

  public Boolean isWithdrawalEnabled() {
    return isWithdrawalEnabled;
  }

  public BigDecimal getMinWithdrawalFee() {
    return minWithdrawalFee;
  }

  public BigDecimal getMaxWithdrawalFee() {
    return maxWithdrawalFee;
  }
}
