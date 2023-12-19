package org.knowm.xchange.mexc.v3.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MexcSymbolV3 {
  private String symbol;
  private String status;
  private String baseAsset;
  private int baseAssetPrecision;
  private String quoteAsset;
  private int quotePrecision;
  private int quoteAssetPrecision;
  private int baseCommissionPrecision;
  private int quoteCommissionPrecision;
  private List<String> orderTypes;
  private boolean isSpotTradingAllowed;
  private boolean isMarginTradingAllowed;
  private String quoteAmountPrecision;
  private String baseSizePrecision;
  private List<String> permissions;
  private List<Object> filters; // Assuming filters can be of any type
  private String maxQuoteAmount;
  private String makerCommission;
  private String takerCommission;
  private String quoteAmountPrecisionMarket;
  private String maxQuoteAmountMarket;
  private String fullName;

  @JsonCreator
  public MexcSymbolV3(
      @JsonProperty("symbol") String symbol,
      @JsonProperty("status") String status,
      @JsonProperty("baseAsset") String baseAsset,
      @JsonProperty("baseAssetPrecision") int baseAssetPrecision,
      @JsonProperty("quoteAsset") String quoteAsset,
      @JsonProperty("quotePrecision") int quotePrecision,
      @JsonProperty("quoteAssetPrecision") int quoteAssetPrecision,
      @JsonProperty("baseCommissionPrecision") int baseCommissionPrecision,
      @JsonProperty("quoteCommissionPrecision") int quoteCommissionPrecision,
      @JsonProperty("orderTypes") List<String> orderTypes,
      @JsonProperty("isSpotTradingAllowed") boolean isSpotTradingAllowed,
      @JsonProperty("isMarginTradingAllowed") boolean isMarginTradingAllowed,
      @JsonProperty("quoteAmountPrecision") String quoteAmountPrecision,
      @JsonProperty("baseSizePrecision") String baseSizePrecision,
      @JsonProperty("permissions") List<String> permissions,
      @JsonProperty("filters") List<Object> filters,
      @JsonProperty("maxQuoteAmount") String maxQuoteAmount,
      @JsonProperty("makerCommission") String makerCommission,
      @JsonProperty("takerCommission") String takerCommission,
      @JsonProperty("quoteAmountPrecisionMarket") String quoteAmountPrecisionMarket,
      @JsonProperty("maxQuoteAmountMarket") String maxQuoteAmountMarket,
      @JsonProperty("fullName") String fullName) {
    this.symbol = symbol;
    this.status = status;
    this.baseAsset = baseAsset;
    this.baseAssetPrecision = baseAssetPrecision;
    this.quoteAsset = quoteAsset;
    this.quotePrecision = quotePrecision;
    this.quoteAssetPrecision = quoteAssetPrecision;
    this.baseCommissionPrecision = baseCommissionPrecision;
    this.quoteCommissionPrecision = quoteCommissionPrecision;
    this.orderTypes = orderTypes;
    this.isSpotTradingAllowed = isSpotTradingAllowed;
    this.isMarginTradingAllowed = isMarginTradingAllowed;
    this.quoteAmountPrecision = quoteAmountPrecision;
    this.baseSizePrecision = baseSizePrecision;
    this.permissions = permissions;
    this.filters = filters;
    this.maxQuoteAmount = maxQuoteAmount;
    this.makerCommission = makerCommission;
    this.takerCommission = takerCommission;
    this.quoteAmountPrecisionMarket = quoteAmountPrecisionMarket;
    this.maxQuoteAmountMarket = maxQuoteAmountMarket;
    this.fullName = fullName;
  }

  public String getSymbol() {
    return symbol;
  }

  public String getStatus() {
    return status;
  }

  public String getBaseAsset() {
    return baseAsset;
  }

  public int getBaseAssetPrecision() {
    return baseAssetPrecision;
  }

  public String getQuoteAsset() {
    return quoteAsset;
  }

  public int getQuotePrecision() {
    return quotePrecision;
  }

  public int getQuoteAssetPrecision() {
    return quoteAssetPrecision;
  }

  public int getBaseCommissionPrecision() {
    return baseCommissionPrecision;
  }

  public int getQuoteCommissionPrecision() {
    return quoteCommissionPrecision;
  }

  public List<String> getOrderTypes() {
    return orderTypes;
  }

  public boolean isSpotTradingAllowed() {
    return isSpotTradingAllowed;
  }

  public boolean isMarginTradingAllowed() {
    return isMarginTradingAllowed;
  }

  public String getQuoteAmountPrecision() {
    return quoteAmountPrecision;
  }

  public String getBaseSizePrecision() {
    return baseSizePrecision;
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public List<Object> getFilters() {
    return filters;
  }

  public String getMaxQuoteAmount() {
    return maxQuoteAmount;
  }

  public String getMakerCommission() {
    return makerCommission;
  }

  public String getTakerCommission() {
    return takerCommission;
  }

  public String getQuoteAmountPrecisionMarket() {
    return quoteAmountPrecisionMarket;
  }

  public String getMaxQuoteAmountMarket() {
    return maxQuoteAmountMarket;
  }

  public String getFullName() {
    return fullName;
  }
}
