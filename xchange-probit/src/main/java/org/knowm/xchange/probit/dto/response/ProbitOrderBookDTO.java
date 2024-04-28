package org.knowm.xchange.probit.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProbitOrderBookDTO {

  private final String side;
  private final BigDecimal price;
  private final BigDecimal quantity;

  @JsonCreator
  public ProbitOrderBookDTO(
      @JsonProperty("side") String side,
      @JsonProperty("price") BigDecimal price,
      @JsonProperty("quantity") BigDecimal quantity) {
    this.side = side;
    this.price = price;
    this.quantity = quantity;
  }

}
