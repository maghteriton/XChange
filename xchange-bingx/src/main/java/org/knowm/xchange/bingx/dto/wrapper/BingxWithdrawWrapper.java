package org.knowm.xchange.bingx.dto.wrapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BingxWithdrawWrapper {

  private final String id;

  @JsonCreator
  public BingxWithdrawWrapper(@JsonProperty("id") String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
