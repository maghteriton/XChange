package org.knowm.xchange.coinw.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class CoinwDepositAddressRequestDTO {

  private String symbolId;
  private String chain;
}
