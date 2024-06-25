package org.knowm.xchange.bitget.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BitgetSymbolsResponse {

  private String symbol;
  private String baseCoin;
  private String quoteCoin;
  private BigDecimal minTradeAmount;
  private BigDecimal maxTradeAmount;
  private BigDecimal takerFeeRate;
  private BigDecimal makerFeeRate;
  private Integer pricePrecision;
  private Integer quantityPrecision;
  private Integer quotePrecision;
  private BigDecimal minTradeUSDT;
  private String status;
  private BigDecimal buyLimitPriceRatio;
  private BigDecimal sellLimitPriceRatio;
}
