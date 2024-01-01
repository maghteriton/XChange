package org.knowm.xchange.bingx;

import org.knowm.xchange.bingx.dto.BingxDepositAddressesDTO;
import org.knowm.xchange.dto.account.DepositAddress;

import java.util.List;
import java.util.stream.Collectors;

public class BingxAdapter {

  public static List<DepositAddress> adaptDepositAddresses(
      List<BingxDepositAddressesDTO> depositAddresses) {
    return depositAddresses.stream()
        .map(
            depAddress ->
                new DepositAddress(
                    depAddress.getCoin(),
                    depAddress.getAddress(),
                    depAddress.getTag(),
                    depAddress.getNetwork()))
        .collect(Collectors.toList());
  }
}
