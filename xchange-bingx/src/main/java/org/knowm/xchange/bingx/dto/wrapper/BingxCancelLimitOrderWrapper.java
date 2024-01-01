package org.knowm.xchange.bingx.dto.wrapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BingxCancelLimitOrderWrapper {

    private final String symbol;
    private final Long orderId;
    private final String price;
    private final String stopPrice;
    private final String origQty;
    private final String executedQty;
    private final String cummulativeQuoteQty;
    private final String status;
    private final String type;
    private final String side;

    @JsonCreator
    public BingxCancelLimitOrderWrapper(
            @JsonProperty("symbol") String symbol,
            @JsonProperty("orderId") long orderId,
            @JsonProperty("price") String price,
            @JsonProperty("stopPrice") String stopPrice,
            @JsonProperty("origQty") String origQty,
            @JsonProperty("executedQty") String executedQty,
            @JsonProperty("cummulativeQuoteQty") String cummulativeQuoteQty,
            @JsonProperty("status") String status,
            @JsonProperty("type") String type,
            @JsonProperty("side") String side) {
        this.symbol = symbol;
        this.orderId = orderId;
        this.price = price;
        this.stopPrice = stopPrice;
        this.origQty = origQty;
        this.executedQty = executedQty;
        this.cummulativeQuoteQty = cummulativeQuoteQty;
        this.status = status;
        this.type = type;
        this.side = side;
    }

    public String getSymbol() {
        return symbol;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getPrice() {
        return price;
    }

    public String getStopPrice() {
        return stopPrice;
    }

    public String getOrigQty() {
        return origQty;
    }

    public String getExecutedQty() {
        return executedQty;
    }

    public String getCummulativeQuoteQty() {
        return cummulativeQuoteQty;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getSide() {
        return side;
    }
}
