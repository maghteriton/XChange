package org.knowm.xchange.bingx.dto.wrapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.knowm.xchange.bingx.dto.BingxBalanceDTO;

public class BingxBalancesWrapper {

  private final List<BingxBalanceDTO> balances;

  @JsonCreator
  public BingxBalancesWrapper(@JsonProperty("balances") List<BingxBalanceDTO> balances) {
    this.balances = balances;
  }

  public List<BingxBalanceDTO> getBalances() {
    return balances;
  }
}
