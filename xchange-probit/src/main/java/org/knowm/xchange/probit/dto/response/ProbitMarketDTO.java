package org.knowm.xchange.probit.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProbitMarketDTO {

  private final String id;
  private final String baseCurrencyId;
  private final String quoteCurrencyId;
  private final BigDecimal minPrice;
  private final BigDecimal maxPrice;
  private final BigDecimal priceIncrement;
  private final BigDecimal minQuantity;
  private final BigDecimal maxQuantity;
  private final int quantityPrecision;
  private final BigDecimal minCost;
  private final BigDecimal maxCost;
  private final Integer costPrecision;
  private final BigDecimal takerFeeRate;
  private final BigDecimal makerFeeRate;
  private final Boolean showInUI;
  private final Boolean closed;

  @JsonCreator
  public ProbitMarketDTO(
      @JsonProperty("id") String id,
      @JsonProperty("base_currency_id") String baseCurrencyId,
      @JsonProperty("quote_currency_id") String quoteCurrencyId,
      @JsonProperty("min_price") BigDecimal minPrice,
      @JsonProperty("max_price") BigDecimal maxPrice,
      @JsonProperty("price_increment") BigDecimal priceIncrement,
      @JsonProperty("min_quantity") BigDecimal minQuantity,
      @JsonProperty("max_quantity") BigDecimal maxQuantity,
      @JsonProperty("quantity_precision") Integer quantityPrecision,
      @JsonProperty("min_cost") BigDecimal minCost,
      @JsonProperty("max_cost") BigDecimal maxCost,
      @JsonProperty("cost_precision") Integer costPrecision,
      @JsonProperty("taker_fee_rate") BigDecimal takerFeeRate,
      @JsonProperty("maker_fee_rate") BigDecimal makerFeeRate,
      @JsonProperty("show_in_ui") Boolean showInUI,
      @JsonProperty("closed") Boolean closed) {
    this.id = id;
    this.baseCurrencyId = baseCurrencyId;
    this.quoteCurrencyId = quoteCurrencyId;
    this.minPrice = minPrice;
    this.maxPrice = maxPrice;
    this.priceIncrement = priceIncrement;
    this.minQuantity = minQuantity;
    this.maxQuantity = maxQuantity;
    this.quantityPrecision = quantityPrecision;
    this.minCost = minCost;
    this.maxCost = maxCost;
    this.costPrecision = costPrecision;
    this.takerFeeRate = takerFeeRate;
    this.makerFeeRate = makerFeeRate;
    this.showInUI = showInUI;
    this.closed = closed;
  }
}
