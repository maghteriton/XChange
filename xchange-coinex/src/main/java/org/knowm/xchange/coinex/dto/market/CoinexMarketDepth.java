package org.knowm.xchange.coinex.dto.market;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@ToString
public class CoinexMarketDepth {

    private final String market;
    private final boolean isFull;
    private final Depth depth;

    public CoinexMarketDepth(
            @JsonProperty("market") String market,
            @JsonProperty("is_full") boolean isFull,
            @JsonProperty("depth") Depth depth) {
        this.market = market;
        this.isFull = isFull;
        this.depth = depth;
    }

    @Getter
    @ToString
    public static class Depth {
        private final List<List<BigDecimal>> asks;
        private final List<List<BigDecimal>> bids;
        private final BigDecimal last;
        private final long updatedAt;
        private final long checksum;

        public Depth(
                @JsonProperty("asks") List<List<BigDecimal>> asks,
                @JsonProperty("bids") List<List<BigDecimal>> bids,
                @JsonProperty("last") BigDecimal last,
                @JsonProperty("updated_at") Long updatedAt,
                @JsonProperty("checksum") Long checksum) {
            this.asks = asks;
            this.bids = bids;
            this.last = last;
            this.updatedAt = updatedAt;
            this.checksum = checksum;
        }
    }
}
