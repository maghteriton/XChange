package org.knowm.xchange.mexc.v3.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Network {
  private final String coin;
  private final String depositDesc;
  private final boolean depositEnable;
  private final int minConfirm;
  private final String name;
  private final String network;
  private final boolean withdrawEnable;
  private final String withdrawFee;
  private final String withdrawIntegerMultiple;
  private final String withdrawMax;
  private final String withdrawMin;
  private final boolean sameAddress;
  private final String contract;
  private final String withdrawTips;
  private final String depositTips;

  public Network(
      @JsonProperty("coin") String coin,
      @JsonProperty("depositDesc") String depositDesc,
      @JsonProperty("depositEnable") boolean depositEnable,
      @JsonProperty("minConfirm") int minConfirm,
      @JsonProperty("name") String name,
      @JsonProperty("network") String network,
      @JsonProperty("withdrawEnable") boolean withdrawEnable,
      @JsonProperty("withdrawFee") String withdrawFee,
      @JsonProperty("withdrawIntegerMultiple") String withdrawIntegerMultiple,
      @JsonProperty("withdrawMax") String withdrawMax,
      @JsonProperty("withdrawMin") String withdrawMin,
      @JsonProperty("sameAddress") boolean sameAddress,
      @JsonProperty("contract") String contract,
      @JsonProperty("withdrawTips") String withdrawTips,
      @JsonProperty("depositTips") String depositTips) {
    this.coin = coin;
    this.depositDesc = depositDesc;
    this.depositEnable = depositEnable;
    this.minConfirm = minConfirm;
    this.name = name;
    this.network = network;
    this.withdrawEnable = withdrawEnable;
    this.withdrawFee = withdrawFee;
    this.withdrawIntegerMultiple = withdrawIntegerMultiple;
    this.withdrawMax = withdrawMax;
    this.withdrawMin = withdrawMin;
    this.sameAddress = sameAddress;
    this.contract = contract;
    this.withdrawTips = withdrawTips;
    this.depositTips = depositTips;
  }

  public String getCoin() {
    return coin;
  }

  public String getDepositDesc() {
    return depositDesc;
  }

  public boolean isDepositEnable() {
    return depositEnable;
  }

  public int getMinConfirm() {
    return minConfirm;
  }

  public String getName() {
    return name;
  }

  public String getNetwork() {
    return network;
  }

  public boolean isWithdrawEnable() {
    return withdrawEnable;
  }

  public String getWithdrawFee() {
    return withdrawFee;
  }

  public String getWithdrawIntegerMultiple() {
    return withdrawIntegerMultiple;
  }

  public String getWithdrawMax() {
    return withdrawMax;
  }

  public String getWithdrawMin() {
    return withdrawMin;
  }

  public boolean isSameAddress() {
    return sameAddress;
  }

  public String getContract() {
    return contract;
  }

  public String getWithdrawTips() {
    return withdrawTips;
  }

  public String getDepositTips() {
    return depositTips;
  }
}
