package org.knowm.xchange.bingx.dto;

import java.util.List;

public class KLineDTO {

  /*
      0   Candlestick chart open time
      1   Open price
      2   Max. price
      3   Min. price
      4   Close price
      5   Filled price
      6   Candlestick chart close time
      7   Volume
  */

  private final List<List<String>> klineData;

  public KLineDTO(List<List<String>> klineData) {
    this.klineData = klineData;
  }

  public List<List<String>> getKlineData() {
    return klineData;
  }
}
