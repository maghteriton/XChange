package org.knowm.xchange.huobi.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;

public class HuobiCurrencyWrapper {

  private final HuobiCurrency[] huobiChains;
  private String currency;
  private final String instStatus;

  public HuobiCurrencyWrapper(
      @JsonProperty("chains") HuobiCurrency[] huobiChains,
      @JsonProperty("currency") String currency,
      @JsonProperty("instStatus") String instStatus) {
    this.huobiChains = huobiChains;
    this.currency = currency;
    this.instStatus = instStatus;
  }

  public HuobiCurrency[] getHuobiChains() {
    return huobiChains;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getInstStatus() {
    return instStatus;
  }

  @Override
  public String toString() {
    return "HuobiCurrencyWrapper{"
        + "huobiCurrencies="
        + Arrays.toString(huobiChains)
        + ", currency='"
        + currency
        + '\''
        + ", instStatus='"
        + instStatus
        + '\''
        + '}';
  }
}
