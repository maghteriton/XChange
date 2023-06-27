package org.knowm.xchange.mexc.dto.market;

import java.math.BigDecimal;

public class MEXCCandleData {

  private final Long time;
  private final BigDecimal open;
  private final BigDecimal close;
  private final BigDecimal high;
  private final BigDecimal low;
  private final BigDecimal vol;
  private final BigDecimal amount;

  public MEXCCandleData(
      Long time,
      BigDecimal open,
      BigDecimal close,
      BigDecimal high,
      BigDecimal low,
      BigDecimal vol,
      BigDecimal amount) {
    this.time = time;
    this.open = open;
    this.close = close;
    this.high = high;
    this.low = low;
    this.vol = vol;
    this.amount = amount;
  }

  public Long getTime() {
    return time;
  }

  public BigDecimal getOpen() {
    return open;
  }

  public BigDecimal getClose() {
    return close;
  }

  public BigDecimal getHigh() {
    return high;
  }

  public BigDecimal getLow() {
    return low;
  }

  public BigDecimal getVol() {
    return vol;
  }

  public BigDecimal getAmount() {
    return amount;
  }
}
