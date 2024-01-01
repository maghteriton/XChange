package org.knowm.xchange.bingx.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class BingxOrderDTO {

  private final String symbol;
  private final Long orderId;
  private final String price;
  private final String stopPrice;
  private final String origQty;
  private final String executedQty;
  private final String cummulativeQuoteQty;
  private final String status;
  private final String type;
  private final String side;
  private final Date time;
  private final Date updateTime;
  private final String origQuoteOrderQty;
  private final String fee;
  private final String feeAsset;

  @JsonCreator
  public BingxOrderDTO(
      @JsonProperty("symbol") String symbol,
      @JsonProperty("orderId") Long orderId,
      @JsonProperty("price") String price,
      @JsonProperty("StopPrice") String stopPrice,
      @JsonProperty("origQty") String origQty,
      @JsonProperty("executedQty") String executedQty,
      @JsonProperty("cummulativeQuoteQty") String cummulativeQuoteQty,
      @JsonProperty("status") String status,
      @JsonProperty("type") String type,
      @JsonProperty("side") String side,
      @JsonProperty("time") Date time,
      @JsonProperty("updateTime") Date updateTime,
      @JsonProperty("origQuoteOrderQty") String origQuoteOrderQty,
      @JsonProperty("fee") String fee,
      @JsonProperty("feeAsset") String feeAsset) {
    this.symbol = symbol;
    this.orderId = orderId;
    this.price = price;
    this.stopPrice = stopPrice;
    this.origQty = origQty;
    this.executedQty = executedQty;
    this.cummulativeQuoteQty = cummulativeQuoteQty;
    this.status = status;
    this.type = type;
    this.side = side;
    this.time = time;
    this.updateTime = updateTime;
    this.origQuoteOrderQty = origQuoteOrderQty;
    this.fee = fee;
    this.feeAsset = feeAsset;
  }

  public String getSymbol() {
    return symbol;
  }

  public Long getOrderId() {
    return orderId;
  }

  public String getPrice() {
    return price;
  }

  public String getStopPrice() {
    return stopPrice;
  }

  public String getOrigQty() {
    return origQty;
  }

  public String getExecutedQty() {
    return executedQty;
  }

  public String getCummulativeQuoteQty() {
    return cummulativeQuoteQty;
  }

  public String getStatus() {
    return status;
  }

  public String getType() {
    return type;
  }

  public String getSide() {
    return side;
  }

  public Date getTime() {
    return time;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public String getOrigQuoteOrderQty() {
    return origQuoteOrderQty;
  }

  public String getFee() {
    return fee;
  }

  public String getFeeAsset() {
    return feeAsset;
  }
}
