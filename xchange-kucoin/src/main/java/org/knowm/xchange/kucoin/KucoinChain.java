package org.knowm.xchange.kucoin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KucoinChain {
  private String chainName;
  private String chain;
  private String withdrawalMinSize;
  private String withdrawalMinFee;
  private Boolean isWithdrawEnabled;
  private Boolean isDepositEnabled;
  private Integer confirms;
  private String contractAddress;

  public String getChainName() {
    return chainName;
  }

  public String getChain() {
    return chain;
  }

  public String getWithdrawalMinSize() {
    return withdrawalMinSize;
  }

  public String getWithdrawalMinFee() {
    return withdrawalMinFee;
  }

  public Boolean isWithdrawEnabled() {
    return isWithdrawEnabled;
  }

  public Boolean isDepositEnabled() {
    return isDepositEnabled;
  }

  public Integer getConfirms() {
    return confirms;
  }

  public String getContractAddress() {
    return contractAddress;
  }
}
