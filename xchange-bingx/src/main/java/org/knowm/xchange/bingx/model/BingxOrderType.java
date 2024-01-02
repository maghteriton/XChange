package org.knowm.xchange.bingx.model;

public enum BingxOrderType {
  LIMIT("LIMIT"),
  MARKET("MARKET");

  private final String value;

  private BingxOrderType(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }
}
