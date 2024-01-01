package org.knowm.xchange.bingx.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class BingxNetworkDTO {

  private final String name;

  private final String network;

  private final Boolean isDefault;

  private final Long minConfirm;

  private final boolean withdrawEnable;

  private final boolean depositEnable;

  private final BigDecimal withdrawFee;

  private final BigDecimal withdrawMax;

  private final BigDecimal withdrawMin;

  private final BigDecimal depositMin;

  private final Long withdrawPrecision;

  private final Long depositPrecision;

  @JsonCreator
  public BingxNetworkDTO(
      @JsonProperty("name") String name,
      @JsonProperty("network") String network,
      @JsonProperty("isDefault") Boolean isDefault,
      @JsonProperty("minConfirm") Long minConfirm,
      @JsonProperty("withdrawEnable") boolean withdrawEnable,
      @JsonProperty("depositEnable") boolean depositEnable,
      @JsonProperty("withdrawFee") BigDecimal withdrawFee,
      @JsonProperty("withdrawMax") BigDecimal withdrawMax,
      @JsonProperty("withdrawMin") BigDecimal withdrawMin,
      @JsonProperty("depositMin") BigDecimal depositMin,
      @JsonProperty("withdrawPrecision") Long withdrawPrecision,
      @JsonProperty("depositPrecision") Long depositPrecision) {
    this.name = name;
    this.network = network;
    this.isDefault = isDefault;
    this.minConfirm = minConfirm;
    this.withdrawEnable = withdrawEnable;
    this.depositEnable = depositEnable;
    this.withdrawFee = withdrawFee;
    this.withdrawMax = withdrawMax;
    this.withdrawMin = withdrawMin;
    this.depositMin = depositMin;
    this.withdrawPrecision = withdrawPrecision;
    this.depositPrecision = depositPrecision;
  }

  public String getName() {
    return name;
  }

  public String getNetwork() {
    return network;
  }

  public Boolean getDefault() {
    return isDefault;
  }

  public Long getMinConfirm() {
    return minConfirm;
  }

  public boolean isWithdrawEnable() {
    return withdrawEnable;
  }

  public boolean isDepositEnable() {
    return depositEnable;
  }

  public BigDecimal getWithdrawFee() {
    return withdrawFee;
  }

  public BigDecimal getWithdrawMax() {
    return withdrawMax;
  }

  public BigDecimal getWithdrawMin() {
    return withdrawMin;
  }

  public BigDecimal getDepositMin() {
    return depositMin;
  }

  public Long getWithdrawPrecision() {
    return withdrawPrecision;
  }

  public Long getDepositPrecision() {
    return depositPrecision;
  }
}
