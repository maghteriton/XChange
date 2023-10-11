package org.knowm.xchange.mexc.v3.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CoinInfo {
  private final String coin;
  private final String name;
  private final List<Network> networkList;

  public CoinInfo(
      @JsonProperty("coin") String coin,
      @JsonProperty("name") String name,
      @JsonProperty("networkList") List<Network> networkList) {
    this.coin = coin;
    this.name = name;
    this.networkList = networkList;
  }

  public String getCoin() {
    return coin;
  }

  public String getName() {
    return name;
  }

  public List<Network> getNetworkList() {
    return networkList;
  }
}
