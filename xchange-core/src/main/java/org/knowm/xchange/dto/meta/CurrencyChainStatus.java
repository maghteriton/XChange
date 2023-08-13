package org.knowm.xchange.dto.meta;


import org.knowm.xchange.currency.Currency;

public class CurrencyChainStatus {

  private final Currency currency;
  private final String chain;
  private final Boolean isDepositEnabled;
  private final Boolean isWithdrawalEnabled;

  public CurrencyChainStatus(
      Currency currency, String chain, Boolean isDepositEnabled, Boolean isWithdrawalEnabled) {
    this.currency = currency;
    this.chain = chain;
    this.isDepositEnabled = isDepositEnabled;
    this.isWithdrawalEnabled = isWithdrawalEnabled;
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
}
