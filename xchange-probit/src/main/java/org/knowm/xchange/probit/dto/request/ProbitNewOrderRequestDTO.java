package org.knowm.xchange.probit.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProbitNewOrderRequestDTO {

  private String market_id;
  private String type;
  private String side;
  private String time_in_force;
  private String limit_price;
  private String quantity;
}
