package org.knowm.xchange.coinw.dto.response;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class CoinwDepositAddressResponseDTO {

  private String minRechargeAmount;
  private String chainName;
  private String address;
}
