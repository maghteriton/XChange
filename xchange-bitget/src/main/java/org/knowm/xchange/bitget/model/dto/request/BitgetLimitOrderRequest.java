package org.knowm.xchange.bitget.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BitgetLimitOrderRequest {

  private String symbol;
  private String side;
  private String orderType;
  private String force;
  private String price;
  private String size;
  private String clientOid;
}
