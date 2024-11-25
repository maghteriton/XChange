package org.knowm.xchange.coinex.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CoinexCancelOrderRequest {

    @JsonProperty("market")
    private final String market;
    @JsonProperty("market_type")
    private final String marketType;
    @JsonProperty("order_id")
    private final Long orderId;

    public CoinexCancelOrderRequest(
            @JsonProperty("market") String market,
            @JsonProperty("market_type") String marketType,
            @JsonProperty("order_id") Long orderId) {
        this.market = market;
        this.marketType = marketType;
        this.orderId = orderId;
    }
}
