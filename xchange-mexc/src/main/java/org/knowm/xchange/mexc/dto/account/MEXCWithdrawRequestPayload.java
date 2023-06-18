package org.knowm.xchange.mexc.dto.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class MEXCWithdrawRequestPayload {

  @JsonProperty("currency")
  private final String currency;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonProperty("chain")
  private final String chain;

  @JsonProperty("amount")
  private final BigDecimal amount;

  @JsonProperty("address")
  private final String address;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonProperty("remark")
  private final String remark;

  public MEXCWithdrawRequestPayload(
      String currency, String chain, BigDecimal amount, String address, String remark) {
    this.currency = currency;
    this.chain = chain;
    this.amount = amount;
    this.address = address;
    this.remark = remark;
  }
}
