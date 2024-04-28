package org.knowm.xchange.probit.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
public class ProbitLimitOrderDTO {

  private final String id;
  private final String userId;
  private final String marketId;
  private final String type;
  private final String side;
  private final BigDecimal quantity;
  private final BigDecimal limitPrice;
  private final String timeInForce;
  private final BigDecimal filledCost;
  private final BigDecimal filledQuantity;
  private final BigDecimal openQuantity;
  private final BigDecimal cancelledQuantity;
  private final String status;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  private final Date time;

  private final String clientOrderId;

  @JsonCreator
  public ProbitLimitOrderDTO(
      @JsonProperty("id") String id,
      @JsonProperty("user_id") String userId,
      @JsonProperty("market_id") String marketId,
      @JsonProperty("type") String type,
      @JsonProperty("side") String side,
      @JsonProperty("quantity") BigDecimal quantity,
      @JsonProperty("limit_price") BigDecimal limitPrice,
      @JsonProperty("time_in_force") String timeInForce,
      @JsonProperty("filled_cost") BigDecimal filledCost,
      @JsonProperty("filled_quantity") BigDecimal filledQuantity,
      @JsonProperty("open_quantity") BigDecimal openQuantity,
      @JsonProperty("cancelled_quantity") BigDecimal cancelledQuantity,
      @JsonProperty("status") String status,
      @JsonProperty("time") Date time,
      @JsonProperty("client_order_id") String clientOrderId) {
    this.id = id;
    this.userId = userId;
    this.marketId = marketId;
    this.type = type;
    this.side = side;
    this.quantity = quantity;
    this.limitPrice = limitPrice;
    this.timeInForce = timeInForce;
    this.filledCost = filledCost;
    this.filledQuantity = filledQuantity;
    this.openQuantity = openQuantity;
    this.cancelledQuantity = cancelledQuantity;
    this.status = status;
    this.time = time;
    this.clientOrderId = clientOrderId;
  }

}
