package org.knowm.xchange.probit.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProbitWithdrawalRequestDTO {

  private String currency_id;
  private String platform_id;
  private String address;
  private String destination_tag;
  private String amount;
  private String fee_currency_id;
}
