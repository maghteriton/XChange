package org.knowm.xchange.mexc.v3.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ExchangeInfo {
    private String timezone;
    private long serverTime;
    private List<MexcSymbolV3> symbols;

    @JsonCreator
    public ExchangeInfo(
            @JsonProperty("timezone") String timezone,
            @JsonProperty("serverTime") long serverTime,
            @JsonProperty("symbols") List<MexcSymbolV3> symbols
    ) {
        this.timezone = timezone;
        this.serverTime = serverTime;
        this.symbols = symbols;
    }

    public String getTimezone() {
        return timezone;
    }

    public long getServerTime() {
        return serverTime;
    }

    public List<MexcSymbolV3> getSymbols() {
        return symbols;
    }
}

