package org.knowm.xchange.bingx.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;

public class BingxOrderDTO {

  private final String symbol;
  private final String orderId;
  private final BigDecimal price;
  private final BigDecimal stopPrice;
  private final BigDecimal origQty;
  private final BigDecimal executedQty;
  private final BigDecimal cummulativeQuoteQty;
  private final String status;
  private final String type;
  private final String side;
  private final Date time;
  private final Date updateTime;
  private final BigDecimal origQuoteOrderQty;
  private final BigDecimal fee;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private final String feeAsset;

  @JsonCreator
  public BingxOrderDTO(
      @JsonProperty("symbol") String symbol,
      @JsonProperty("orderId") String orderId,
      @JsonProperty("price") BigDecimal price,
      @JsonProperty("StopPrice") BigDecimal stopPrice,
      @JsonProperty("origQty") BigDecimal origQty,
      @JsonProperty("executedQty") BigDecimal executedQty,
      @JsonProperty("cummulativeQuoteQty") BigDecimal cummulativeQuoteQty,
      @JsonProperty("status") String status,
      @JsonProperty("type") String type,
      @JsonProperty("side") String side,
      @JsonProperty("time") Date time,
      @JsonProperty("updateTime") Date updateTime,
      @JsonProperty("origQuoteOrderQty") BigDecimal origQuoteOrderQty,
      @JsonProperty("fee") BigDecimal fee,
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

  public String getOrderId() {
    return orderId;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public BigDecimal getStopPrice() {
    return stopPrice;
  }

  public BigDecimal getOrigQty() {
    return origQty;
  }

  public BigDecimal getExecutedQty() {
    return executedQty;
  }

  public BigDecimal getCummulativeQuoteQty() {
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

  public BigDecimal getOrigQuoteOrderQty() {
    return origQuoteOrderQty;
  }

  public BigDecimal getFee() {
    return fee;
  }

  public String getFeeAsset() {
    return feeAsset;
  }
}
