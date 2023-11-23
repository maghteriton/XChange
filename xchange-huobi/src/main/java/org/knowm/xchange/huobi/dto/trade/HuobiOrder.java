package org.knowm.xchange.huobi.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;

public class HuobiOrder {

  private final long accountID;
  private final BigDecimal amount;
  private final Date canceledAt;
  private final Date createdAt;
  private final BigDecimal filledAmount;
  private final BigDecimal filledCashAmount;
  private final BigDecimal filledFees;
  private final Date finishedAt;
  private final long id;
  private final BigDecimal price;
  private final String source;
  private final String state;
  private final String symbol;
  private final String type;
  private final String clOrdId;
  private final BigDecimal stopPrice;
  private final String operator;

  public HuobiOrder(
      @JsonProperty("account-id") long accountID,
      @JsonProperty("amount") BigDecimal amount,
      @JsonProperty("canceled-at") Date canceledAt,
      @JsonProperty("created-at") Date createdAt,
      @JsonProperty("field-amount") BigDecimal filledAmount,
      @JsonProperty("field-cash-amount") BigDecimal filledCashAmount,
      @JsonProperty("field-fees") BigDecimal filledFees,
      @JsonProperty("finished-at") Date finishedAt,
      @JsonProperty("id") long id,
      @JsonProperty("price") BigDecimal price,
      @JsonProperty("source") String source,
      @JsonProperty("state") String state,
      @JsonProperty("symbol") String symbol,
      @JsonProperty("type") String type,
      @JsonProperty("client-order-id") String clOrdId,
      @JsonProperty("stop-price") BigDecimal stopPrice,
      @JsonProperty("operator") String operator) {
    this.accountID = accountID;
    this.amount = amount;
    this.canceledAt = canceledAt != null && canceledAt.getTime() != 0 ? canceledAt : null;
    this.createdAt = createdAt;
    this.filledAmount = filledAmount;
    this.filledCashAmount = filledCashAmount;
    this.filledFees = filledFees;
    this.finishedAt = finishedAt != null && finishedAt.getTime() != 0 ? finishedAt : null;
    this.id = id;
    this.price = price;
    this.source = source;
    this.state = state;
    this.symbol = symbol;
    this.type = type;
    this.clOrdId = clOrdId;
    this.stopPrice = stopPrice;
    this.operator = operator;
  }

  public long getAccountID() {
    return accountID;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Date getCanceledAt() {
    return canceledAt;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public BigDecimal getFilledAmount() {
    return filledAmount;
  }

  public BigDecimal getFilledCashAmount() {
    return filledCashAmount;
  }

  public BigDecimal getFilledFees() {
    return filledFees;
  }

  public Date getFinishedAt() {
    return finishedAt;
  }

  public long getId() {
    return id;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public String getSource() {
    return source;
  }

  public String getState() {
    return state;
  }

  public String getSymbol() {
    return symbol;
  }

  public String getType() {
    return type;
  }

  public String getClOrdId() {
    return clOrdId;
  }

  public BigDecimal getStopPrice() {
    return stopPrice;
  }

  public String getOperator() {
    return operator;
  }

  public boolean isLimit() { // startswith to support -fok and -ioc
    return getType().startsWith("buy-limit") || getType().startsWith("sell-limit");
  }

  public boolean isMarket() {
    return getType().equals("buy-market") || getType().equals("sell-market");
  }

  public boolean isStop() {
    return getType().startsWith("buy-stop") || getType().startsWith("sell-stop");
  }

  @Override
  public String toString() {
    return "HuobiOrder{"
        + "accountID="
        + accountID
        + ", amount="
        + amount
        + ", canceledAt="
        + canceledAt
        + ", createdAt="
        + createdAt
        + ", filledAmount="
        + filledAmount
        + ", filledCashAmount="
        + filledCashAmount
        + ", filledFees="
        + filledFees
        + ", finishedAt="
        + finishedAt
        + ", id="
        + id
        + ", price="
        + price
        + ", source='"
        + source
        + '\''
        + ", state='"
        + state
        + '\''
        + ", symbol='"
        + symbol
        + '\''
        + ", type='"
        + type
        + '\''
        + ", clOrdId='"
        + clOrdId
        + '\''
        + ", stopPrice="
        + stopPrice
        + ", operator='"
        + operator
        + '\''
        + '}';
  }
}
