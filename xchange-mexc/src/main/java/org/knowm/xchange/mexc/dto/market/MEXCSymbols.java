package org.knowm.xchange.mexc.dto.market;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MEXCSymbols {

  private final String symbol;
  private final String state;
  private final Integer priceScale;
  private final Integer quantityScale;
  private final String minAmount;
  private final String maxAmount;
  private final String makerFeeRate;
  private final String takerFeeRate;
  private final String limited;
  private final Integer etfMark;
  private final String symbolPartition;

  public MEXCSymbols(
      @JsonProperty("symbol") String symbol,
      @JsonProperty("state") String state,
      @JsonProperty("price_scale") Integer priceScale,
      @JsonProperty("quantity_scale") Integer quantityScale,
      @JsonProperty("min_amount") String minAmount,
      @JsonProperty("max_amount") String maxAmount,
      @JsonProperty("maker_fee_rate") String makerFeeRate,
      @JsonProperty("taker_fee_rate") String takerFeeRate,
      @JsonProperty("limited") String limited,
      @JsonProperty("etf_mark") Integer etfMark,
      @JsonProperty("symbol_partition") String symbolPartition) {
    this.symbol = symbol;
    this.state = state;
    this.priceScale = priceScale;
    this.quantityScale = quantityScale;
    this.minAmount = minAmount;
    this.maxAmount = maxAmount;
    this.makerFeeRate = makerFeeRate;
    this.takerFeeRate = takerFeeRate;
    this.limited = limited;
    this.etfMark = etfMark;
    this.symbolPartition = symbolPartition;
  }

  public String getSymbol() {
    return symbol;
  }

  public String getState() {
    return state;
  }

  public Integer getPriceScale() {
    return priceScale;
  }

  public Integer getQuantityScale() {
    return quantityScale;
  }

  public String getMinAmount() {
    return minAmount;
  }

  public String getMaxAmount() {
    return maxAmount;
  }

  public String getMakerFeeRate() {
    return makerFeeRate;
  }

  public String getTakerFeeRate() {
    return takerFeeRate;
  }

  public Boolean getLimited() {
    return Boolean.parseBoolean(limited);
  }

  public Integer getEtfMark() {
    return etfMark;
  }

  public String getSymbolPartition() {
    return symbolPartition;
  }
}
