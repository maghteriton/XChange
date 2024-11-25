package org.knowm.xchange.coinex.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CoinexOrderRequestPayload {

    @JsonProperty("market")
    private final String market;
    @JsonProperty("market_type")
    private final String marketType;
    @JsonProperty("side")
    private final String side;
    @JsonProperty("type")
    private final String type;
    @JsonProperty("amount")
    private final String amount;
    @JsonProperty("price")
    private final String price;

    public CoinexOrderRequestPayload(
            @JsonProperty("market") String market,
            @JsonProperty("market_type") String marketType,
            @JsonProperty("side") String side,
            @JsonProperty("type") String type,
            @JsonProperty("amount") String amount,
            @JsonProperty("price") String price) {
        this.market = market;
        this.marketType = marketType;
        this.side = side;
        this.type = type;
        this.amount = amount;
        this.price = price;
    }
}
