package org.knowm.xchange.bingx.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BingxDepositAddressesDTO {

  private final Integer coinId;
  private final String coin;
  private final String network;
  private final String address;
  private final String tag;

  @JsonCreator
  public BingxDepositAddressesDTO(
      @JsonProperty("coinId") Integer coinId,
      @JsonProperty("coin") String coin,
      @JsonProperty("network") String network,
      @JsonProperty("address") String address,
      @JsonProperty("tag") String tag) {
    this.coinId = coinId;
    this.coin = coin;
    this.network = network;
    this.address = address;
    this.tag = tag;
  }

  public Integer getCoinId() {
    return coinId;
  }

  public String getCoin() {
    return coin;
  }

  public String getNetwork() {
    return network;
  }

  public String getAddress() {
    return address;
  }

  public String getTag() {
    return tag;
  }
}
