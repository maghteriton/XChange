package org.knowm.xchange.bingx.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class BingxBalanceDTO {

  private final String asset;

  private final String free;

  private final String locked;

  @JsonCreator
  public BingxBalanceDTO(
      @JsonProperty("asset") String asset,
      @JsonProperty("free") String free,
      @JsonProperty("locked") String locked) {
    this.asset = asset;
    this.free = free;
    this.locked = locked;
  }

  public String getAsset() {
    return asset;
  }

  public String getFree() {
    return free;
  }

  public String getLocked() {
    return locked;
  }
}
