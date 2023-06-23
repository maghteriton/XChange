package org.knowm.xchange.mexc.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MEXCDepositAddress {

  private final String currency;
  private final List<MEXCChain> mexcChainList;

  public MEXCDepositAddress(
      @JsonProperty("currency") String currency,
      @JsonProperty("chains") List<MEXCChain> mexcChainList) {
    this.currency = currency;
    this.mexcChainList = mexcChainList;
  }

  public String getCurrency() {
    return currency;
  }

  public List<MEXCChain> getMexcChainList() {
    return mexcChainList;
  }
}
