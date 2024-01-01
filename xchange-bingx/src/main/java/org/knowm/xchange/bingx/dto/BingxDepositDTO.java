package org.knowm.xchange.bingx.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class BingxDepositDTO {

  private final String amount;

  private final String coin;

  private final String network;

  private final Integer status;

  private final String address;

  private final String addressTag;

  private final String txId;

  private final Date insertTime;

  private final String unlockConfirm;

  private final String confirmTimes;

  @JsonCreator
  public BingxDepositDTO(
      @JsonProperty("amount") String amount,
      @JsonProperty("coin") String coin,
      @JsonProperty("network") String network,
      @JsonProperty("status") Integer status,
      @JsonProperty("address") String address,
      @JsonProperty("addressTag") String addressTag,
      @JsonProperty("txId") String txId,
      @JsonProperty("insertTime") Date insertTime,
      @JsonProperty("unlockConfirm") String unlockConfirm,
      @JsonProperty("confirmTimes") String confirmTimes) {
    this.amount = amount;
    this.coin = coin;
    this.network = network;
    this.status = status;
    this.address = address;
    this.addressTag = addressTag;
    this.txId = txId;
    this.insertTime = insertTime;
    this.unlockConfirm = unlockConfirm;
    this.confirmTimes = confirmTimes;
  }

  public String getAmount() {
    return amount;
  }

  public String getCoin() {
    return coin;
  }

  public String getNetwork() {
    return network;
  }

  public Integer getStatus() {
    return status;
  }

  public String getAddress() {
    return address;
  }

  public String getAddressTag() {
    return addressTag;
  }

  public String getTxId() {
    return txId;
  }

  public Date getInsertTime() {
    return insertTime;
  }

  public String getUnlockConfirm() {
    return unlockConfirm;
  }

  public String getConfirmTimes() {
    return confirmTimes;
  }
}
