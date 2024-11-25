package org.knowm.xchange.coinex.dto;

import lombok.Getter;

import static java.util.concurrent.TimeUnit.*;

@Getter
public enum CoinexPeriod {
    _1MIN("1min", MINUTES.toSeconds(1)),
    _3MIN("3min", MINUTES.toSeconds(3)),
    _5MIN("5min", MINUTES.toSeconds(5)),
    _15MIN("15min", MINUTES.toSeconds(15)),
    _30MIN("30min", MINUTES.toSeconds(30)),
    _1HOUR("1hour", HOURS.toSeconds(1)),
    _2HOUR("2hour", HOURS.toSeconds(2)),
    _4HOUR("4hour", HOURS.toSeconds(4)),
    _6HOUR("6hour", HOURS.toSeconds(6)),
    _12HOUR("12hour", HOURS.toSeconds(12)),
    _1DAY("1day", DAYS.toSeconds(1)),
    _3DAY("3day", DAYS.toSeconds(3)),
    _1WEEK("1week", DAYS.toSeconds(7));

    private final String period;
    private final long seconds;

    CoinexPeriod(String period, long seconds) {
        this.period = period;
        this.seconds = seconds;
    }
}