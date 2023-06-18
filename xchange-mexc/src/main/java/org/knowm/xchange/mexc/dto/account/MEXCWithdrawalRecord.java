package org.knowm.xchange.mexc.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class MEXCWithdrawalRecord {

  private final String id;
  private final String currency;
  private final String address;
  private final BigDecimal amount;
  private final BigDecimal fee;
  private final String remark;
  private final String state;
  private final String txId;
  private final String explorerUrl;
  private final String updateTime;
  private final String createTime;
  private final String trans_hash;

  public MEXCWithdrawalRecord(
      @JsonProperty("id") String id,
      @JsonProperty("currency") String currency,
      @JsonProperty("address") String address,
      @JsonProperty("amount") BigDecimal amount,
      @JsonProperty("fee") BigDecimal fee,
      @JsonProperty("remark") String remark,
      @JsonProperty("state") String state,
      @JsonProperty("tx_id") String txId,
      @JsonProperty("explorer_url") String explorerUrl,
      @JsonProperty("update_time") String updateTime,
      @JsonProperty("create_time") String createTime,
      @JsonProperty("trans_hash") String transHash) {
    this.id = id;
    this.currency = currency;
    this.address = address;
    this.amount = amount;
    this.fee = fee;
    this.remark = remark;
    this.state = state;
    this.txId = txId;
    this.explorerUrl = explorerUrl;
    this.updateTime = updateTime;
    this.createTime = createTime;
    this.trans_hash = transHash;
  }

  public String getId() {
    return id;
  }

  public String getCurrency() {
    return currency;
  }

  public String getAddress() {
    return address;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public BigDecimal getFee() {
    return fee;
  }

  public String getRemark() {
    return remark;
  }

  public String getState() {
    return state;
  }

  public String getTxId() {
    return txId;
  }

  public String getExplorerUrl() {
    return explorerUrl;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public String getCreateTime() {
    return createTime;
  }

  public String getTrans_hash() {
    return trans_hash;
  }
}
