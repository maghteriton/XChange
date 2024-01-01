package org.knowm.xchange.bingx.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class BingxSymbolDTO {

  private final String symbol;

  private final BigDecimal minimumQuantity;

  private final BigDecimal maximumQuantity;

  private final BigDecimal minimumNotional;

  private final BigDecimal maximumNotional;

  private final Integer status;

  private final BigDecimal tickSize;

  private final BigDecimal stepSize;

  private final Boolean apiStateSell;

  private final Boolean apiStateBuy;

  private final Long timeOnline;

  @JsonCreator
  public BingxSymbolDTO(
      @JsonProperty("symbol") String symbol,
      @JsonProperty("minQty") BigDecimal minimumQuantity,
      @JsonProperty("maxQty") BigDecimal maximumQuantity,
      @JsonProperty("minNotional") BigDecimal minimumNotional,
      @JsonProperty("maxNotional") BigDecimal maximumNotional,
      @JsonProperty("status") Integer status,
      @JsonProperty("tickSize") BigDecimal tickSize,
      @JsonProperty("stepSize") BigDecimal stepSize,
      @JsonProperty("apiStateSell") Boolean apiStateSell,
      @JsonProperty("apiStateBuy") Boolean apiStateBuy,
      @JsonProperty("timeOnline") Long timeOnline) {
    this.symbol = symbol;
    this.minimumQuantity = minimumQuantity;
    this.maximumQuantity = maximumQuantity;
    this.minimumNotional = minimumNotional;
    this.maximumNotional = maximumNotional;
    this.status = status;
    this.tickSize = tickSize;
    this.stepSize = stepSize;
    this.apiStateSell = apiStateSell;
    this.apiStateBuy = apiStateBuy;
    this.timeOnline = timeOnline;
  }

  public String getSymbol() {
    return symbol;
  }

  public BigDecimal getMinimumQuantity() {
    return minimumQuantity;
  }

  public BigDecimal getMaximumQuantity() {
    return maximumQuantity;
  }

  public BigDecimal getMinimumNotional() {
    return minimumNotional;
  }

  public BigDecimal getMaximumNotional() {
    return maximumNotional;
  }

  public Integer getStatus() {
    return status;
  }

  public BigDecimal getTickSize() {
    return tickSize;
  }

  public BigDecimal getStepSize() {
    return stepSize;
  }

  public Boolean isApiStateSell() {
    return apiStateSell;
  }

  public Boolean isApiStateBuy() {
    return apiStateBuy;
  }

  public Long getTimeOnline() {
    return timeOnline;
  }
}
