package org.knowm.xchange.bitget.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BitgetCancelLimitOrderRequest {

  private String symbol;
  private String orderId;
}
