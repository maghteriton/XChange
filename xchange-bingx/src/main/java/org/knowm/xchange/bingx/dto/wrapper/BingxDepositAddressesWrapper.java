package org.knowm.xchange.bingx.dto.wrapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.bingx.dto.BingxDepositAddressesDTO;

import java.util.List;

public class BingxDepositAddressesWrapper {

  private final List<BingxDepositAddressesDTO> data;

  private final Integer total;

  @JsonCreator
  public BingxDepositAddressesWrapper(
      @JsonProperty("data") List<BingxDepositAddressesDTO> data,
      @JsonProperty("total") Integer total) {
    this.data = data;
    this.total = total;
  }

  public List<BingxDepositAddressesDTO> getData() {
    return data;
  }

  public Integer getTotal() {
    return total;
  }
}
