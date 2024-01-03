package org.knowm.xchange.bingx.dto.wrapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class BingxCreateLimitOrderWrapper {

  private final String symbol;

  private final String orderId;

  private final Date transactTime;

  private final String price;

  private final String origQty;

  private final String executedQty;

  private final String cummulativeQuoteQty;

  private final String status;

  private final String type;

  private final String side;

  @JsonCreator
  public BingxCreateLimitOrderWrapper(
      @JsonProperty("symbol") String symbol,
      @JsonProperty("orderId") String orderId,
      @JsonProperty("transactTime") Date transactTime,
      @JsonProperty("price") String price,
      @JsonProperty("origQty") String origQty,
      @JsonProperty("executedQty") String executedQty,
      @JsonProperty("cummulativeQuoteQty") String cummulativeQuoteQty,
      @JsonProperty("status") String status,
      @JsonProperty("type") String type,
      @JsonProperty("side") String side) {
    this.symbol = symbol;
    this.orderId = orderId;
    this.transactTime = transactTime;
    this.price = price;
    this.origQty = origQty;
    this.executedQty = executedQty;
    this.cummulativeQuoteQty = cummulativeQuoteQty;
    this.status = status;
    this.type = type;
    this.side = side;
  }

  public String getSymbol() {
    return symbol;
  }

  public String getOrderId() {
    return orderId;
  }

  public Date getTransactTime() {
    return transactTime;
  }

  public String getPrice() {
    return price;
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
}
