package org.knowm.xchange.probit.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProbitWithdrawalFeeDTO {

  private final String currencyId;
  private final BigDecimal amount;
  private final Integer priority;

  @JsonCreator
  public ProbitWithdrawalFeeDTO(
      @JsonProperty("currency_id") String currencyId,
      @JsonProperty("amount") BigDecimal amount,
      @JsonProperty("priority") Integer priority) {
    this.currencyId = currencyId;
    this.amount = amount;
    this.priority = priority;
  }
}
