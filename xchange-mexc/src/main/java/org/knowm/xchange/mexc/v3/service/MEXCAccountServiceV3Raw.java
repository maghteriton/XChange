package org.knowm.xchange.mexc.v3.service;

import java.io.IOException;
import java.util.List;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.mexc.v3.dto.CoinInfo;

public class MEXCAccountServiceV3Raw extends MEXCBaseServiceV3 {

  public MEXCAccountServiceV3Raw(Exchange exchange) {
    super(exchange);
  }

  public List<CoinInfo> getAllCoins() throws IOException {
    return mexcAuthenticatedV3.getCurrencyInformation(apiKeyV3, signatureCreatorV3, nonceFactoryV3);
  }
}
