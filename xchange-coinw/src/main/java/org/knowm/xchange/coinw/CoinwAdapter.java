package org.knowm.xchange.coinw;

import com.google.common.collect.Ordering;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.DepositAddress;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.marketdata.CandleStick;
import org.knowm.xchange.dto.marketdata.CandleStickData;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.dto.meta.WalletHealth;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.instrument.Instrument;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;
import static org.knowm.xchange.dto.Order.OrderStatus.*;
import static org.knowm.xchange.dto.Order.OrderStatus.REJECTED;
import static org.knowm.xchange.dto.Order.OrderType.ASK;
import static org.knowm.xchange.dto.Order.OrderType.BID;

public class CoinwAdapter {

  private static final SimpleDateFormat DATE_FORMATTER =
      new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

  private static final String ORDER_TYPE_SELL = "sell";
  private static final String ORDER_TYPE_BUY = "buy";
  private static final String DEFAULT_TRADE_FEE = "0.002";

  private CoinwAdapter() {
    // hides the public one
  }

  public static String formatDate(Date date) {
    return date != null ? DATE_FORMATTER.format(date) : null;
  }

  public static CurrencyPair adaptFromProbitSymbol(String probitSymbol) {
    String[] splitSymbol = probitSymbol.split("-");
    return new CurrencyPair(splitSymbol[0], splitSymbol[1]);
  }

  private static LimitOrder adaptLimitOrder(
      CurrencyPair currencyPair,
      Order.OrderType orderType,
      BigDecimal price,
      BigDecimal size,
      Date timestamp) {
    return new LimitOrder.Builder(orderType, currencyPair)
        .limitPrice(price)
        .originalAmount(size)
        .orderStatus(NEW)
        .timestamp(timestamp)
        .build();
  }

  public static String adaptCurrencyPair(CurrencyPair currencyPair) {
    return currencyPair.toString().replace("/", "-");
  }

  public static String adaptToProbitSide(Order.OrderType type) {
    return type == ASK ? ORDER_TYPE_SELL : ORDER_TYPE_BUY;
  }

  public static Order.OrderType adaptFromProbitSide(String probitSide) {
    return probitSide.equalsIgnoreCase(ORDER_TYPE_SELL) ? ASK : BID;
  }

  private static Order.OrderStatus adaptFromProbitOrderStatus(String orderStatus) {
    if (orderStatus == null) {
      return null;
    }
    switch (orderStatus) {
      case "open":
        return OPEN;
      case "filled":
        return FILLED;
      case "cancelled":
        return CANCELED;
      default:
        throw new ExchangeException("Not supported status: " + orderStatus);
    }
  }

/*  public static ExchangeMetaData adaptToExchangeMetaData(ExchangeMetaData exchangeMetaData, List<CoinwMarketDTO> marketData, List<CoinwCurrencyDTO> currency) {
    return null;
  }*/
}
