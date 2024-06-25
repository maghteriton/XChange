/** Copyright 2019 Mek Global Limited. */
package org.knowm.xchange.bitget.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import lombok.Data;

/** Created by tao.mao on 2018/11/15. */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BitgetAccountBalancesResponse {

  private String id;

  private String currency;

  private String type;

  private BigDecimal balance;

  private BigDecimal available;

  private BigDecimal holds;
}
