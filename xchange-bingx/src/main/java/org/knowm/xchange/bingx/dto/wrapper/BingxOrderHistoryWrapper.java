package org.knowm.xchange.bingx.dto.wrapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.bingx.dto.BingxOrderDTO;

import java.util.List;

public class BingxOrderHistoryWrapper {

  private final List<BingxOrderDTO> orders;

  @JsonCreator
  public BingxOrderHistoryWrapper(@JsonProperty("orders") List<BingxOrderDTO> orders) {
    this.orders = orders;
  }

  public List<BingxOrderDTO> getOrders() {
    return orders;
  }
}
