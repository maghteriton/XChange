package org.knowm.xchange.mexc.dto.market;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;

public class MEXCDepth {

  public final List<MEXCDepthEntry> bids;
  public final List<MEXCDepthEntry> asks;

  public MEXCDepth(
      @JsonProperty("bids") List<MEXCDepthEntry> bids,
      @JsonProperty("asks") List<MEXCDepthEntry> asks) {
    this.bids = bids;
    this.asks = asks;
  }

  public List<MEXCDepthEntry> getBids() {
    return bids;
  }

  public List<MEXCDepthEntry> getAsks() {
    return asks;
  }
}
