package org.knowm.xchange.mexc.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MEXCChain {

  private final String chain;
  private final String address;
  private final String realAddress;
  private final String memo;

  public MEXCChain(
      @JsonProperty("chain") String chain,
      @JsonProperty("address") String address,
      @JsonProperty("realAddress") String realAddress,
      @JsonProperty("memo") String memo) {
    this.chain = chain;
    this.address = address;
    this.realAddress = realAddress;
    this.memo = memo;
  }

  public String getChain() {
    return chain;
  }

  public String getAddress() {
    return address;
  }

  public String getRealAddress() {
    return realAddress;
  }

  public String getMemo() {
    return memo;
  }
}
