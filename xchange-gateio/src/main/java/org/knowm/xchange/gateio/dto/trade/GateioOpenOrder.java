package org.knowm.xchange.gateio.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class GateioOpenOrder {

  private String timestamp;

  private String total;

  private BigDecimal amount;

  private BigDecimal filledRate;

  private BigDecimal filledAmount;

  private String id;

  private BigDecimal rate;

  private String status;

  private String orderNumber;

  private String type;

  private String currencyPair;

  /** Constructor */
  private GateioOpenOrder(
      @JsonProperty("timestamp") String timestamp,
      @JsonProperty("total") String total,
      @JsonProperty("amount") BigDecimal amount,
      @JsonProperty("filledRate") BigDecimal filledRate,
      @JsonProperty("filledAmount") BigDecimal filledAmount,
      @JsonProperty("id") String id,
      @JsonProperty("rate") BigDecimal rate,
      @JsonProperty("status") String status,
      @JsonProperty("orderNumber") String orderNumber,
      @JsonProperty("type") String type,
      @JsonProperty("currencyPair") String currencyPair) {
    this.timestamp = timestamp;
    this.total = total;
    this.amount = amount;
    this.filledRate = filledRate;
    this.filledAmount = filledAmount;
    this.id = id;
    this.rate = rate;
    this.status = status;
    this.orderNumber = orderNumber;
    this.type = type;
    this.currencyPair = currencyPair;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public String getTotal() {
    return total;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public BigDecimal getFilledRate() {
    return filledRate;
  }

  public BigDecimal getFilledAmount() {
    return filledAmount;
  }

  public String getId() {
    return id;
  }

  public BigDecimal getRate() {
    return rate;
  }

  public String getStatus() {
    return status;
  }

  public String getOrderNumber() {
    return orderNumber;
  }

  public String getType() {
    return type;
  }

  public String getCurrencyPair() {
    return currencyPair;
  }

  @Override
  public String toString() {
    return "GateioOpenOrder{"
        + "timestamp='"
        + timestamp
        + '\''
        + ", total='"
        + total
        + '\''
        + ", amount='"
        + amount
        + '\''
        + ", filledRate='"
        + filledRate
        + '\''
        + ", filledAmount='"
        + filledAmount
        + '\''
        + ", id='"
        + id
        + '\''
        + ", rate='"
        + rate
        + '\''
        + ", status='"
        + status
        + '\''
        + ", orderNumber='"
        + orderNumber
        + '\''
        + ", type='"
        + type
        + '\''
        + ", currencyPair='"
        + currencyPair
        + '\''
        + '}';
  }
}
