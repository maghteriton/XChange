package org.knowm.xchange.probit.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ProbitDepositAddressDTO {

  private final String currencyId;
  private final String address;
  private final String addressTag;

  @JsonCreator
  public ProbitDepositAddressDTO(
      @JsonProperty("currency_id") String currencyId,
      @JsonProperty("address") String address,
      @JsonProperty("destination_tag") String addressTag) {
    this.currencyId = currencyId;
    this.address = address;
    this.addressTag = addressTag;
  }

}
