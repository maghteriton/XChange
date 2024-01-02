package org.knowm.xchange.bingx.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class BingxMarketDepthDTO {

  private final List<List<String>> bids;

  private final List<List<String>> asks;

  private final Date ts;

  @JsonCreator
  public BingxMarketDepthDTO(
      @JsonProperty("bids") List<List<String>> bids,
      @JsonProperty("asks") List<List<String>> asks,
      @JsonProperty("ts") Date ts) {
    this.bids = bids;
    this.asks = asks;
    this.ts = ts;
  }

  public List<List<String>> getBids() {
    return bids;
  }

  public List<List<String>> getAsks() {
    return asks;
  }

  public Date getTs() {
    return ts;
  }
}
