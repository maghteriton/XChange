package org.knowm.xchange.probit.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
public class ProbitCurrencyDTO {
  private final String id;
  private final String name;
  private final Map<String, String> displayName;
  private final String platform;
  private final Integer precision;
  private final Integer displayPrecision;
  private final Integer minConfirmationCount;
  private final BigDecimal minWithdrawalAmount;
  private final List<ProbitWithdrawalFeeDTO> withdrawalFeeList;
  private final Boolean depositSuspended;
  private final Boolean withdrawalSuspended;
  private final Integer internalPrecision;
  private final Boolean showInUI;
  private final String suspendedReason;
  private final String minDepositAmount;

  @JsonCreator
  public ProbitCurrencyDTO(
      @JsonProperty("id") String id,
      @JsonProperty("name") String name,
      @JsonProperty("display_name") Map<String, String> displayName,
      @JsonProperty("platform") String platform,
      @JsonProperty("precision") Integer precision,
      @JsonProperty("display_precision") Integer displayPrecision,
      @JsonProperty("min_confirmation_count") Integer minConfirmationCount,
      @JsonProperty("min_withdrawal_amount") BigDecimal minWithdrawalAmount,
      @JsonProperty("withdrawal_fee") List<ProbitWithdrawalFeeDTO> withdrawalFeeList,
      @JsonProperty("deposit_suspended") Boolean depositSuspended,
      @JsonProperty("withdrawal_suspended") Boolean withdrawalSuspended,
      @JsonProperty("internal_precision") Integer internalPrecision,
      @JsonProperty("show_in_ui") Boolean showInUI,
      @JsonProperty("suspended_reason") String suspendedReason,
      @JsonProperty("min_deposit_amount") String minDepositAmount) {
    this.id = id;
    this.name = name;
    this.displayName = displayName;
    this.platform = platform;
    this.precision = precision;
    this.displayPrecision = displayPrecision;
    this.minConfirmationCount = minConfirmationCount;
    this.minWithdrawalAmount = minWithdrawalAmount;
    this.withdrawalFeeList = withdrawalFeeList;
    this.depositSuspended = depositSuspended;
    this.withdrawalSuspended = withdrawalSuspended;
    this.internalPrecision = internalPrecision;
    this.showInUI = showInUI;
    this.suspendedReason = suspendedReason;
    this.minDepositAmount = minDepositAmount;
  }

}
