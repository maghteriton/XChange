package org.knowm.xchange.bingx.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TradeCommissionRateDTO {

  private final BigDecimal takerCommissionRate;
  private final BigDecimal makerCommissionRate;

  @JsonCreator
  public TradeCommissionRateDTO(
      @JsonProperty("takerCommissionRate") BigDecimal takerCommissionRate,
      @JsonProperty("makerCommissionRate") BigDecimal makerCommissionRate) {
    this.takerCommissionRate = takerCommissionRate;
    this.makerCommissionRate = makerCommissionRate;
  }

  public BigDecimal getTakerCommissionRate() {
    return takerCommissionRate;
  }

  public BigDecimal getMakerCommissionRate() {
    return makerCommissionRate;
  }
}
