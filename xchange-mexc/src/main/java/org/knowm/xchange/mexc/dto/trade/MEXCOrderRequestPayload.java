package org.knowm.xchange.mexc.dto.trade;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MEXCOrderRequestPayload {

  @JsonProperty("symbol")
  private final String symbol;

  @JsonProperty("price")
  private final String price;

  @JsonProperty("quantity")
  private final String quantity;

  @JsonProperty("trade_type")
  private final String tradeType;

  @JsonProperty("order_type")
  private final String orderType;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonProperty("client_order_id")
  private final String clientOrderId;

  public MEXCOrderRequestPayload(
      String symbol,
      String price,
      String quantity,
      String tradeType,
      String orderType,
      String clientOrderId) {
    this.symbol = symbol;
    this.price = price;
    this.quantity = quantity;
    this.tradeType = tradeType;
    this.orderType = orderType;
    this.clientOrderId = clientOrderId;
  }
}
