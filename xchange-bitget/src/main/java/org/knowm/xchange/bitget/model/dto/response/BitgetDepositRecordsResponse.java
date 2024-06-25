package org.knowm.xchange.bitget.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BitgetDepositRecordsResponse {

  private String orderId;
  private String tradeId;
  private String coin;
  private String type;
  private BigDecimal size;
  private String status;
  private String toAddress;
  private String dest;
  private String chain;
  private String fromAddress;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Date cTime;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Date uTime;
}
