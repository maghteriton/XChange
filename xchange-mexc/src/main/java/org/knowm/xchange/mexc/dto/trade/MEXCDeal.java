package org.knowm.xchange.mexc.dto.trade;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MEXCDeal {

  private final String symbol;
  private final String id;
  private final String orderId;
  private final String tradeType;
  private final String quantity;
  private final String price;
  private final String amount;
  private final String fee;
  private final String feeCurrency;
  private final Boolean isTaker;
  private final Long createTime;

  @JsonCreator
  public MEXCDeal(
      @JsonProperty("symbol") String symbol,
      @JsonProperty("id") String id,
      @JsonProperty("order_id") String orderId,
      @JsonProperty("trade_type") String tradeType,
      @JsonProperty("quantity") String quantity,
      @JsonProperty("price") String price,
      @JsonProperty("amount") String amount,
      @JsonProperty("fee") String fee,
      @JsonProperty("fee_currency") String feeCurrency,
      @JsonProperty("is_taker") Boolean isTaker,
      @JsonProperty("create_time") Long createTime) {
    this.symbol = symbol;
    this.id = id;
    this.orderId = orderId;
    this.tradeType = tradeType;
    this.quantity = quantity;
    this.price = price;
    this.amount = amount;
    this.fee = fee;
    this.feeCurrency = feeCurrency;
    this.isTaker = isTaker;
    this.createTime = createTime;
  }

  public String getSymbol() {
    return symbol;
  }

  public String getId() {
    return id;
  }

  public String getOrderId() {
    return orderId;
  }

  public String getTradeType() {
    return tradeType;
  }

  public String getQuantity() {
    return quantity;
  }

  public String getPrice() {
    return price;
  }

  public String getAmount() {
    return amount;
  }

  public String getFee() {
    return fee;
  }

  public String getFeeCurrency() {
    return feeCurrency;
  }

  public Boolean getTaker() {
    return isTaker;
  }

  public Long getCreateTime() {
    return createTime;
  }
}
