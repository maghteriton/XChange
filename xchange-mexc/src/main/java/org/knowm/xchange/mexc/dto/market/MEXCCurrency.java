package org.knowm.xchange.mexc.dto.market;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class MEXCCurrency {

  private final String chain;
  private final BigDecimal precision;
  private final BigDecimal withdrawalFee;
  private final String isWithdrawEnabled;
  private final String isDepositEnabled;
  private final BigDecimal depositMinConfirm;
  private final BigDecimal withdrawLimitMax;
  private final BigDecimal withdrawLimitMin;

  public MEXCCurrency(
      @JsonProperty("chain") String chain,
      @JsonProperty("precision") BigDecimal precision,
      @JsonProperty("fee") BigDecimal withdrawalFee,
      @JsonProperty("is_withdraw_enabled") String isWithdrawEnabled,
      @JsonProperty("is_deposit_enabled") String isDepositEnabled,
      @JsonProperty("deposit_min_confirm") BigDecimal depositMinConfirm,
      @JsonProperty("withdraw_limit_max") BigDecimal withdrawLimitMax,
      @JsonProperty("withdraw_limit_min") BigDecimal withdrawLimitMin) {
    this.chain = chain;
    this.precision = precision;
    this.withdrawalFee = withdrawalFee;
    this.isWithdrawEnabled = isWithdrawEnabled;
    this.isDepositEnabled = isDepositEnabled;
    this.depositMinConfirm = depositMinConfirm;
    this.withdrawLimitMax = withdrawLimitMax;
    this.withdrawLimitMin = withdrawLimitMin;
  }

  public String getChain() {
    return chain;
  }

  public BigDecimal getPrecision() {
    return precision;
  }

  public BigDecimal getWithdrawalFee() {
    return withdrawalFee;
  }

  public Boolean getIsWithdrawEnabled() {
    return Boolean.valueOf(isWithdrawEnabled);
  }

  public Boolean getIsDepositEnabled() {
    return Boolean.valueOf(isDepositEnabled);
  }

  public BigDecimal getDepositMinConfirm() {
    return depositMinConfirm;
  }

  public BigDecimal getWithdrawLimitMax() {
    return withdrawLimitMax;
  }

  public BigDecimal getWithdrawLimitMin() {
    return withdrawLimitMin;
  }
}
