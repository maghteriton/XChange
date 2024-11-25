package org.knowm.xchange.coinex.dto.market;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class CoinexKlineData {

    private final String market;
    private final long createdAt;
    private final BigDecimal open;
    private final BigDecimal close;
    private final BigDecimal high;
    private final BigDecimal low;
    private final BigDecimal volume;
    private final BigDecimal value;

    public CoinexKlineData(
            @JsonProperty("market") String market,
            @JsonProperty("created_at") long createdAt,
            @JsonProperty("open") BigDecimal open,
            @JsonProperty("close") BigDecimal close,
            @JsonProperty("high") BigDecimal high,
            @JsonProperty("low") BigDecimal low,
            @JsonProperty("volume") BigDecimal volume,
            @JsonProperty("value") BigDecimal value) {
        this.market = market;
        this.createdAt = createdAt;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
        this.value = value;
    }
}
