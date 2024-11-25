package org.knowm.xchange.coinex.dto.trade;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.math.BigDecimal;

@Getter
@ToString
public class CoinexOrderResponse {

    private final Long orderId;
    private final String market;
    private final String marketType;
    private final String side;
    private final String type;
    private final String ccy;
    private final BigDecimal amount;
    private final BigDecimal price;
    private final BigDecimal unfilledAmount;
    private final BigDecimal filledAmount;
    private final BigDecimal filledValue;
    private final String clientId;
    private final BigDecimal baseFee;
    private final BigDecimal quoteFee;
    private final BigDecimal discountFee;
    private final BigDecimal makerFeeRate;
    private final BigDecimal takerFeeRate;
    private final BigDecimal lastFilledAmount;
    private final BigDecimal lastFilledPrice;
    private final Long createdAt;
    private final Long updatedAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String status;

    public CoinexOrderResponse(
            @JsonProperty("order_id") Long orderId,
            @JsonProperty("market") String market,
            @JsonProperty("market_type") String marketType,
            @JsonProperty("side") String side,
            @JsonProperty("type") String type,
            @JsonProperty("ccy") String ccy,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("price") BigDecimal price,
            @JsonProperty("unfilled_amount") BigDecimal unfilledAmount,
            @JsonProperty("filled_amount") BigDecimal filledAmount,
            @JsonProperty("filled_value") BigDecimal filledValue,
            @JsonProperty("client_id") String clientId,
            @JsonProperty("base_fee") BigDecimal baseFee,
            @JsonProperty("quote_fee") BigDecimal quoteFee,
            @JsonProperty("discount_fee") BigDecimal discountFee,
            @JsonProperty("maker_fee_rate") BigDecimal makerFeeRate,
            @JsonProperty("taker_fee_rate") BigDecimal takerFeeRate,
            @JsonProperty("last_fill_amount") BigDecimal lastFilledAmount,
            @JsonProperty("last_fill_price") BigDecimal lastFilledPrice,
            @JsonProperty("created_at") Long createdAt,
            @JsonProperty("updated_at") Long updatedAt,
            @JsonProperty("status") String status
    ) {
        this.orderId = orderId;
        this.market = market;
        this.marketType = marketType;
        this.side = side;
        this.type = type;
        this.ccy = ccy;
        this.amount = amount;
        this.price = price;
        this.unfilledAmount = unfilledAmount;
        this.filledAmount = filledAmount;
        this.filledValue = filledValue;
        this.clientId = clientId;
        this.baseFee = baseFee;
        this.quoteFee = quoteFee;
        this.discountFee = discountFee;
        this.makerFeeRate = makerFeeRate;
        this.takerFeeRate = takerFeeRate;
        this.lastFilledAmount = lastFilledAmount;
        this.lastFilledPrice = lastFilledPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }
}
