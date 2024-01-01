package org.knowm.xchange.bingx;

import static java.util.concurrent.TimeUnit.*;

public enum KLineInterval {
  min1("1m"),
  min3("3m"),
  min5("5m"),
  min15("15m"),
  min30("30m"),

  hour1("1h"),
  hour2("2h"),
  hour4("4h"),
  hour6("6h"),
  hour8("8h"),
  hour12("12hr"),

  day1("1d"),
  day3("3d"),

  week1("1w"),
  month1("1m");

  private final String code;

  private KLineInterval(String code) {
    this.code = code;
  }

  public String code() {
    return code;
  }
}
