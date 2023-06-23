package org.knowm.xchange.mexc.dto.market;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MEXCCurrencyInfo {

  private final String currency;
  private final List<MEXCCurrency> mexcCurrencyList;

  public MEXCCurrencyInfo(
      @JsonProperty("currency") String currency,
      @JsonProperty("coins") List<MEXCCurrency> mexcCurrencyList) {
    this.currency = currency;
    this.mexcCurrencyList = mexcCurrencyList;
  }

  public String getCurrency() {
    return currency;
  }

  public List<MEXCCurrency> getMexcCoinList() {
    return mexcCurrencyList;
  }
}
