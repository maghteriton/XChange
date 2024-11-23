package org.knowm.xchange.coinex.dto.market;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CoinexMarketInfo {

    private final String market;
    private final String makerFeeRate;
    private final String takerFeeRate;
    private final String minAmount;
    private final String baseCcy;
    private final String quoteCcy;
    private final int baseCcyPrecision;
    private final int quoteCcyPrecision;
    private final boolean isAmmAvailable;
    private final boolean isMarginAvailable;

    public CoinexMarketInfo(
            @JsonProperty("market") String market,
            @JsonProperty("maker_fee_rate") String makerFeeRate,
            @JsonProperty("taker_fee_rate") String takerFeeRate,
            @JsonProperty("min_amount") String minAmount,
            @JsonProperty("base_ccy") String baseCcy,
            @JsonProperty("quote_ccy") String quoteCcy,
            @JsonProperty("base_ccy_precision") int baseCcyPrecision,
            @JsonProperty("quote_ccy_precision") int quoteCcyPrecision,
            @JsonProperty("is_amm_available") boolean isAmmAvailable,
            @JsonProperty("is_margin_available") boolean isMarginAvailable) {
        this.market = market;
        this.makerFeeRate = makerFeeRate;
        this.takerFeeRate = takerFeeRate;
        this.minAmount = minAmount;
        this.baseCcy = baseCcy;
        this.quoteCcy = quoteCcy;
        this.baseCcyPrecision = baseCcyPrecision;
        this.quoteCcyPrecision = quoteCcyPrecision;
        this.isAmmAvailable = isAmmAvailable;
        this.isMarginAvailable = isMarginAvailable;
    }
}
