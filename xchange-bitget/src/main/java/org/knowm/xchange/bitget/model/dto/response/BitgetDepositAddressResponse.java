package org.knowm.xchange.bitget.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BitgetDepositAddressResponse {
  private String address;
  private String chain;
  private String coin;
  private String tag;
  private String url;
}
