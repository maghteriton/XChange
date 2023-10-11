package org.knowm.xchange.gateio.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.gateio.GateioAdapters;
import org.knowm.xchange.gateio.dto.GateioOrderType;

import java.math.BigDecimal;

public class GateioClosedOrder {
    private final String orderNumber;
    private final String status;
    private final CurrencyPair currencyPair;
    private final GateioOrderType type;
    private final String rate;
    private final double left;
    private final String amount;
    private final String initialRate;
    private final String initialAmount;
    private final String filledAmount;
    private final BigDecimal filledRate;
    private final double feePercentage;
    private final String feeValue;
    private final String feeCurrency;
    private final String fee;
    private final long timestamp;

    // Constructor with all properties
    public GateioClosedOrder(
            @JsonProperty("orderNumber") String orderNumber,
            @JsonProperty("status") String status,
            @JsonProperty("currencyPair") String currencyPair,
            @JsonProperty("type") GateioOrderType type,
            @JsonProperty("rate") String rate,
            @JsonProperty("left") double left,
            @JsonProperty("amount") String amount,
            @JsonProperty("initialRate") String initialRate,
            @JsonProperty("initialAmount") String initialAmount,
            @JsonProperty("filledAmount") String filledAmount,
            @JsonProperty("filledRate") BigDecimal filledRate,
            @JsonProperty("feePercentage") double feePercentage,
            @JsonProperty("feeValue") String feeValue,
            @JsonProperty("feeCurrency") String feeCurrency,
            @JsonProperty("fee") String fee,
            @JsonProperty("timestamp") long timestamp
    ) {
        this.orderNumber = orderNumber;
        this.status = status;
        this.currencyPair = GateioAdapters.adaptCurrencyPair(currencyPair);
        this.type = type;
        this.rate = rate;
        this.left = left;
        this.amount = amount;
        this.initialRate = initialRate;
        this.initialAmount = initialAmount;
        this.filledAmount = filledAmount;
        this.filledRate = filledRate;
        this.feePercentage = feePercentage;
        this.feeValue = feeValue;
        this.feeCurrency = feeCurrency;
        this.fee = fee;
        this.timestamp = timestamp;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getStatus() {
        return status;
    }

    public CurrencyPair getCurrencyPair() {
        return currencyPair;
    }

    public GateioOrderType getType() {
        return type;
    }

    public String getRate() {
        return rate;
    }

    public double getLeft() {
        return left;
    }

    public String getAmount() {
        return amount;
    }

    public String getInitialRate() {
        return initialRate;
    }

    public String getInitialAmount() {
        return initialAmount;
    }

    public String getFilledAmount() {
        return filledAmount;
    }

    public BigDecimal getFilledRate() {
        return filledRate;
    }

    public double getFeePercentage() {
        return feePercentage;
    }

    public String getFeeValue() {
        return feeValue;
    }

    public String getFeeCurrency() {
        return feeCurrency;
    }

    public String getFee() {
        return fee;
    }

    public long getTimestamp() {
        return timestamp;
    }
}