package org.knowm.xchange.bitget.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BitgetWithdrawalRequest {

  private final String coin;
  private final String transferType;
  private final String address;
  private final String tag;
  private final String chain;
  private final String size;
}
