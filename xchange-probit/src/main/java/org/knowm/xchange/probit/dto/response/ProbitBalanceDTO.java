package org.knowm.xchange.probit.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProbitBalanceDTO {

  private final String currencyId;
  private final BigDecimal total;
  private final BigDecimal available;

  @JsonCreator
  public ProbitBalanceDTO(
      @JsonProperty("currency_id") String currencyId,
      @JsonProperty("total") BigDecimal total,
      @JsonProperty("available") BigDecimal available) {
    this.currencyId = currencyId;
    this.total = total;
    this.available = available;
  }

}
