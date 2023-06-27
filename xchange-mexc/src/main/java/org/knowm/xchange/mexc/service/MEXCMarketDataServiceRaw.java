package org.knowm.xchange.mexc.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.mexc.MEXCAdapters;
import org.knowm.xchange.mexc.dto.market.MEXCCandleData;
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

  public List<MEXCCandleData> getMEXCCandleStickData(
      CurrencyPair currencyPair, String interval, Long after, Integer limit) throws IOException {
    List<List<String>> mexcCandleData =
        mexcAuthenticated
            .getCandleStickData(
                MEXCAdapters.convertToMEXCSymbol(currencyPair), interval, after, limit)
            .getData();

    return mexcCandleData.stream()
        .map(
            data ->
                new MEXCCandleData(
                    Long.parseLong(data.get(0)),
                    new BigDecimal(data.get(1)),
                    new BigDecimal(data.get(2)),
                    new BigDecimal(data.get(3)),
                    new BigDecimal(data.get(4)),
                    new BigDecimal(data.get(5)),
                    new BigDecimal(data.get(6))))
        .collect(Collectors.toList());
  }
}
