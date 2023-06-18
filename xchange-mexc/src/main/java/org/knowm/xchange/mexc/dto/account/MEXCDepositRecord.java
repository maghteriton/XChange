package org.knowm.xchange.mexc.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class MEXCDepositRecord {
  private final String currency;
  private final BigDecimal amount;
  private final BigDecimal fee;
  private final Integer confirmations;
  private final String address;
  private final String state;
  private final String txId;
  private final Integer requireConfirmations;
  private final String createTime;
  private final String updateTime;
  private final String transHash;

  public MEXCDepositRecord(
      @JsonProperty("currency") String currency,
      @JsonProperty("amount") BigDecimal amount,
      @JsonProperty("fee") BigDecimal fee,
      @JsonProperty("confirmations") Integer confirmations,
      @JsonProperty("address") String address,
      @JsonProperty("state") String state,
      @JsonProperty("tx_id") String txId,
      @JsonProperty("require_confirmations") Integer requireConfirmations,
      @JsonProperty("create_time") String createTime,
      @JsonProperty("update_time") String updateTime,
      @JsonProperty("trans_hash") String transHash) {
    this.currency = currency;
    this.amount = amount;
    this.fee = fee;
    this.confirmations = confirmations;
    this.address = address;
    this.state = state;
    this.txId = txId;
    this.requireConfirmations = requireConfirmations;
    this.createTime = createTime;
    this.updateTime = updateTime;
    this.transHash = transHash;
  }

  public String getCurrency() {
    return currency;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public BigDecimal getFee() {
    return fee;
  }

  public Integer getConfirmations() {
    return confirmations;
  }

  public String getAddress() {
    return address;
  }

  public String getState() {
    return state;
  }

  public String getTxId() {
    return txId;
  }

  public Integer getRequireConfirmations() {
    return requireConfirmations;
  }

  public String getCreateTime() {
    return createTime;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public String getTransHash() {
    return transHash;
  }
}
