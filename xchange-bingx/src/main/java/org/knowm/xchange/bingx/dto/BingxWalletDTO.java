package org.knowm.xchange.bingx.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BingxWalletDTO {

  private final String coin;

  private final String name;

  private final List<BingxNetworkDTO> networkList;

  @JsonCreator
  public BingxWalletDTO(
      @JsonProperty("coin") String coin,
      @JsonProperty("name") String name,
      @JsonProperty("networkList") List<BingxNetworkDTO> networkList) {
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

  public List<BingxNetworkDTO> getNetworkList() {
    return networkList;
  }
}
