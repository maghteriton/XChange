package org.knowm.xchange.mexc.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MEXCWithdrawal {

  private final String withdrawId;

  public MEXCWithdrawal(@JsonProperty("withdrawId") String withdrawId) {
    this.withdrawId = withdrawId;
  }

  public String getWithdrawId() {
    return withdrawId;
  }
}
