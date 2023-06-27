package org.knowm.xchange.mexc.dto.market;

import static java.util.concurrent.TimeUnit.*;

// 1m, 5m, 15m, 30m, 60m. of hour: 4h, of day: 1d, of month: 1M
public enum MEXCKlineInterval {
  m1("1m", MINUTES.toSeconds(1)),
  m5("5m", MINUTES.toSeconds(5)),
  m15("15m", MINUTES.toSeconds(15)),
  m30("30m", MINUTES.toSeconds(30)),
  m60("60m", HOURS.toSeconds(1)),
  h4("4h", HOURS.toSeconds(4)),
  d1("1d", DAYS.toSeconds(1)),
  M1("1M", DAYS.toSeconds(30));

  private final String code;
  private final Long seconds;

  private MEXCKlineInterval(String code, Long seconds) {
    this.seconds = seconds;
    this.code = code;
  }

  public Long getSeconds() {
    return seconds;
  }

  public String getCode() {
    return code;
  }
}
