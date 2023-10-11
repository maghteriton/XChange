package org.knowm.xchange.gateio.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.gateio.dto.GateioBaseResponse;

/** Created by David Henry on 2/19/14. */
public class GateioOrderStatus extends GateioBaseResponse {

  private final GateioClosedOrder gateioClosedOrder;

  private GateioOrderStatus(
      @JsonProperty("order") GateioClosedOrder gateioClosedOrder,
      @JsonProperty("result") boolean result,
      @JsonProperty("message") String msg) {

    super(result, msg);
    this.gateioClosedOrder = gateioClosedOrder;
  }

  public GateioClosedOrder getGateioClosedOrder() {
    return gateioClosedOrder;
  }
}
