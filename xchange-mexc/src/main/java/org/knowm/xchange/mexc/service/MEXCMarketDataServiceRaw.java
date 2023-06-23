package org.knowm.xchange.mexc.service;

import java.io.IOException;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.mexc.MEXCAdapters;
import org.knowm.xchange.mexc.dto.market.MEXCDepth;

public class MEXCMarketDataServiceRaw extends MEXCBaseService {
  public MEXCMarketDataServiceRaw(Exchange exchange) {
    super(exchange);
  }

  public MEXCDepth getMEXCDepth(CurrencyPair currencyPair, String size) throws IOException {
    return mexcAuthenticated
        .getMarketDepth(
            apiKey,
            nonceFactory,
            signatureCreator,
            MEXCAdapters.convertToMEXCSymbol(currencyPair),
            size)
        .getData();
  }
}
