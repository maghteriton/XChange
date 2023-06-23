package org.knowm.xchange.mexc.dto.market;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class MEXCDepthEntry {

  private final BigDecimal price;
  private final BigDecimal quantity;

  public MEXCDepthEntry(
      @JsonProperty("price") BigDecimal price, @JsonProperty("quantity") BigDecimal quantity) {
    this.price = price;
    this.quantity = quantity;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public BigDecimal getQuantity() {
    return quantity;
  }
}
