package org.knowm.xchange.huobi.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HuobiChain {
  @JsonProperty("adt")
  private final boolean hasAddrDepositTag;

  @JsonProperty("ac")
  private final String addressOfChain;

  @JsonProperty("ao")
  private final boolean addrOneOff;

  @JsonProperty("awt")
  private final boolean addrWithTag;

  @JsonProperty("chain")
  private final String chain;

  @JsonProperty("ct")
  private final String chainType;

  @JsonProperty("code")
  private final String code;

  @JsonProperty("currency")
  private final String currency;

  @JsonProperty("deposit-desc")
  private final String depositDesc;

  @JsonProperty("de")
  private final boolean depositEnable;

  @JsonProperty("dma")
  private final String depositMinAmount;

  @JsonProperty("deposit-tips-desc")
  private final String depositTipsDesc;

  @JsonProperty("dn")
  private final String displayName;

  @JsonProperty("fc")
  private final int fastConfirms;

  @JsonProperty("ft")
  private final String feeType;

  @JsonProperty("default")
  private final int isDefault;

  @JsonProperty("replace-chain-info-desc")
  private final String replaceChainInfoDesc;

  @JsonProperty("replace-chain-notification-desc")
  private final String replaceChainNotificationDesc;

  @JsonProperty("replace-chain-popup-desc")
  private final String replaceChainPopupDesc;

  @JsonProperty("ca")
  private final String contractAddress;

  @JsonProperty("cct")
  private final int contractChainType;

  @JsonProperty("sc")
  private final int safeConfirms;

  @JsonProperty("sda")
  private final String suspendDepositAnnouncement;

  @JsonProperty("suspend-deposit-desc")
  private final String suspendDepositDesc;

  @JsonProperty("swa")
  private final String suspendWithdrawAnnouncement;

  @JsonProperty("suspend-withdraw-desc")
  private final String suspendWithdrawDesc;

  @JsonProperty("v")
  private final boolean visible;

  @JsonProperty("withdraw-desc")
  private final String withdrawDesc;

  @JsonProperty("we")
  private final boolean withdrawEnable;

  @JsonProperty("wma")
  private final String withdrawMinAmount;

  @JsonProperty("wp")
  private final int withdrawPrecision;

  @JsonProperty("fn")
  private final String fn;

  @JsonProperty("withdraw-tips-desc")
  private final String withdrawTipsDesc;

  @JsonProperty("suspend-visible-desc")
  private final String suspendVisibleDesc;

  public HuobiChain(
      @JsonProperty("adt") boolean hasAddrDepositTag,
      @JsonProperty("ac") String addressOfChain,
      @JsonProperty("ao") boolean addrOneOff,
      @JsonProperty("awt") boolean addrWithTag,
      @JsonProperty("chain") String chain,
      @JsonProperty("ct") String chainType,
      @JsonProperty("code") String code,
      @JsonProperty("currency") String currency,
      @JsonProperty("deposit-desc") String depositDesc,
      @JsonProperty("de") boolean depositEnable,
      @JsonProperty("dma") String depositMinAmount,
      @JsonProperty("deposit-tips-desc") String depositTipsDesc,
      @JsonProperty("dn") String displayName,
      @JsonProperty("fc") int fastConfirms,
      @JsonProperty("ft") String feeType,
      @JsonProperty("default") int isDefault,
      @JsonProperty("replace-chain-info-desc") String replaceChainInfoDesc,
      @JsonProperty("replace-chain-notification-desc") String replaceChainNotificationDesc,
      @JsonProperty("replace-chain-popup-desc") String replaceChainPopupDesc,
      @JsonProperty("ca") String contractAddress,
      @JsonProperty("cct") int contractChainType,
      @JsonProperty("sc") int safeConfirms,
      @JsonProperty("sda") String suspendDepositAnnouncement,
      @JsonProperty("suspend-deposit-desc") String suspendDepositDesc,
      @JsonProperty("swa") String suspendWithdrawAnnouncement,
      @JsonProperty("suspend-withdraw-desc") String suspendWithdrawDesc,
      @JsonProperty("v") boolean visible,
      @JsonProperty("withdraw-desc") String withdrawDesc,
      @JsonProperty("we") boolean withdrawEnable,
      @JsonProperty("wma") String withdrawMinAmount,
      @JsonProperty("wp") int withdrawPrecision,
      @JsonProperty("fn") String fn,
      @JsonProperty("withdraw-tips-desc") String withdrawTipsDesc,
      @JsonProperty("suspend-visible-desc") String suspendVisibleDesc) {
    this.hasAddrDepositTag = hasAddrDepositTag;
    this.addressOfChain = addressOfChain;
    this.addrOneOff = addrOneOff;
    this.addrWithTag = addrWithTag;
    this.chain = chain;
    this.chainType = chainType;
    this.code = code;
    this.currency = currency;
    this.depositDesc = depositDesc;
    this.depositEnable = depositEnable;
    this.depositMinAmount = depositMinAmount;
    this.depositTipsDesc = depositTipsDesc;
    this.displayName = displayName;
    this.fastConfirms = fastConfirms;
    this.feeType = feeType;
    this.isDefault = isDefault;
    this.replaceChainInfoDesc = replaceChainInfoDesc;
    this.replaceChainNotificationDesc = replaceChainNotificationDesc;
    this.replaceChainPopupDesc = replaceChainPopupDesc;
    this.contractAddress = contractAddress;
    this.contractChainType = contractChainType;
    this.safeConfirms = safeConfirms;
    this.suspendDepositAnnouncement = suspendDepositAnnouncement;
    this.suspendDepositDesc = suspendDepositDesc;
    this.suspendWithdrawAnnouncement = suspendWithdrawAnnouncement;
    this.suspendWithdrawDesc = suspendWithdrawDesc;
    this.visible = visible;
    this.withdrawDesc = withdrawDesc;
    this.withdrawEnable = withdrawEnable;
    this.withdrawMinAmount = withdrawMinAmount;
    this.withdrawPrecision = withdrawPrecision;
    this.fn = fn;
    this.withdrawTipsDesc = withdrawTipsDesc;
    this.suspendVisibleDesc = suspendVisibleDesc;
  }

  public boolean isHasAddrDepositTag() {
    return hasAddrDepositTag;
  }

  public String getAddressOfChain() {
    return addressOfChain;
  }

  public boolean isAddrOneOff() {
    return addrOneOff;
  }

  public boolean isAddrWithTag() {
    return addrWithTag;
  }

  public String getChain() {
    return chain;
  }

  public String getChainType() {
    return chainType;
  }

  public String getCode() {
    return code;
  }

  public String getCurrency() {
    return currency;
  }

  public String getDepositDesc() {
    return depositDesc;
  }

  public boolean isDepositEnable() {
    return depositEnable;
  }

  public String getDepositMinAmount() {
    return depositMinAmount;
  }

  public String getDepositTipsDesc() {
    return depositTipsDesc;
  }

  public String getDisplayName() {
    return displayName;
  }

  public int getFastConfirms() {
    return fastConfirms;
  }

  public String getFeeType() {
    return feeType;
  }

  public int getIsDefault() {
    return isDefault;
  }

  public String getReplaceChainInfoDesc() {
    return replaceChainInfoDesc;
  }

  public String getReplaceChainNotificationDesc() {
    return replaceChainNotificationDesc;
  }

  public String getReplaceChainPopupDesc() {
    return replaceChainPopupDesc;
  }

  public String getContractAddress() {
    return contractAddress;
  }

  public int getContractChainType() {
    return contractChainType;
  }

  public int getSafeConfirms() {
    return safeConfirms;
  }

  public String getSuspendDepositAnnouncement() {
    return suspendDepositAnnouncement;
  }

  public String getSuspendDepositDesc() {
    return suspendDepositDesc;
  }

  public String getSuspendWithdrawAnnouncement() {
    return suspendWithdrawAnnouncement;
  }

  public String getSuspendWithdrawDesc() {
    return suspendWithdrawDesc;
  }

  public boolean isVisible() {
    return visible;
  }

  public String getWithdrawDesc() {
    return withdrawDesc;
  }

  public boolean isWithdrawEnable() {
    return withdrawEnable;
  }

  public String getWithdrawMinAmount() {
    return withdrawMinAmount;
  }

  public int getWithdrawPrecision() {
    return withdrawPrecision;
  }

  public String getFn() {
    return fn;
  }

  public String getWithdrawTipsDesc() {
    return withdrawTipsDesc;
  }

  public String getSuspendVisibleDesc() {
    return suspendVisibleDesc;
  }
}
