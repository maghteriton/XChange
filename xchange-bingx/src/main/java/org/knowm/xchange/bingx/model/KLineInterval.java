package org.knowm.xchange.bingx.model;

import static java.util.concurrent.TimeUnit.*;

public enum KLineInterval {
  min1("1m", MINUTES.toSeconds(1)),
  min3("3m", MINUTES.toSeconds(3)),
  min5("5m", MINUTES.toSeconds(5)),
  min15("15m", MINUTES.toSeconds(15)),
  min30("30m", MINUTES.toSeconds(30)),

  hour1("1h", HOURS.toSeconds(1)),
  hour2("2h", HOURS.toSeconds(2)),
  hour4("4h", HOURS.toSeconds(4)),
  hour6("6h", HOURS.toSeconds(6)),
  hour8("8h", HOURS.toSeconds(8)),
  hour12("12hr", HOURS.toSeconds(12)),

  day1("1d", DAYS.toSeconds(1)),
  day3("3d", DAYS.toSeconds(3)),

  week1("1w", DAYS.toSeconds(7)),
  month1("1m", DAYS.toSeconds(30));

  private final String code;
  private final Long seconds;

  private KLineInterval(String code, Long seconds) {
    this.seconds = seconds;
    this.code = code;
  }

  public Long getSeconds() {
    return seconds;
  }

  public String code() {
    return code;
  }
}
