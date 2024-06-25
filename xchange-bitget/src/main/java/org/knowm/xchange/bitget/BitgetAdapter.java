package org.knowm.xchange.bitget;

import com.google.common.collect.Ordering;
import org.knowm.xchange.bitget.model.dto.response.*;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.DepositAddress;
import org.knowm.xchange.dto.account.FundingRecord;
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
import org.knowm.xchange.bitget.model.dto.response.BitgetOrderHistoryResponse.BitgetFeeDetail.FeeDetailItem;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;
import static org.knowm.xchange.dto.Order.OrderStatus.*;
import static org.knowm.xchange.dto.Order.OrderType.ASK;
import static org.knowm.xchange.dto.Order.OrderType.BID;

public class BitgetAdapter {

  private static final String DEPOSIT = "deposit";
  private static final String BUY = "buy";
  private static final String SELL = "sell";

  public static DepositAddress adaptBitgetDeposit(
      BitgetDepositAddressResponse bitgetDepositAddress) {

    return bitgetDepositAddress != null
        ? new DepositAddress(
            bitgetDepositAddress.getCoin(),
            bitgetDepositAddress.getAddress(),
            bitgetDepositAddress.getTag(),
            bitgetDepositAddress.getChain())
        : null;
  }

  public static ExchangeMetaData adaptMetadata(
      List<BitgetCoinsResponse> bitgetCoins, List<BitgetSymbolsResponse> bitgetSymbols) {

    Map<Instrument, InstrumentMetaData> pairs = new HashMap<>();
    for (BitgetSymbolsResponse bitgetSymbolsResponse : bitgetSymbols) {
      CurrencyPair currencyPair =
          new CurrencyPair(
              bitgetSymbolsResponse.getBaseCoin(), bitgetSymbolsResponse.getQuoteCoin());
      pairs.put(currencyPair, adaptPair(bitgetSymbolsResponse));
    }

    Map<Currency, CurrencyMetaData> currencies = new HashMap<>();
    for (BitgetCoinsResponse bitgetCoinsResponse : bitgetCoins) {
      List<BitgetCoinsResponse.Chain> chains = bitgetCoinsResponse.getChains();
      if (!chains.isEmpty()) {
        BitgetCoinsResponse.Chain chain = chains.get(0);
        CurrencyMetaData currencyMetaData =
            new CurrencyMetaData(
                Integer.valueOf(chain.getWithdrawMinScale()),
                chain.getWithdrawFee(),
                chain.getMinWithdrawAmount(),
                getWalletHealthStatus(chain));
        currencies.put(new Currency(bitgetCoinsResponse.getCoin()), currencyMetaData);
      }
    }

    return new ExchangeMetaData(pairs, currencies, null, null, null);
  }

  public static InstrumentMetaData adaptPair(BitgetSymbolsResponse bitgetSymbol) {
    return new InstrumentMetaData.Builder()
        .tradingFee(bitgetSymbol.getTakerFeeRate())
        .minimumAmount(bitgetSymbol.getMinTradeAmount())
        .priceScale(bitgetSymbol.getPricePrecision())
        .volumeScale(bitgetSymbol.getQuantityPrecision())
        .feeTiers(null)
        .build();
  }

  private static WalletHealth getWalletHealthStatus(BitgetCoinsResponse.Chain bitgetChain) {
    WalletHealth walletHealth = WalletHealth.ONLINE;
    if (!bitgetChain.getRechargeable() && !bitgetChain.getWithdrawable()) {
      walletHealth = WalletHealth.OFFLINE;
    } else if (!bitgetChain.getRechargeable()) {
      walletHealth = WalletHealth.DEPOSITS_DISABLED;
    } else if (!bitgetChain.getWithdrawable()) {
      walletHealth = WalletHealth.WITHDRAWALS_DISABLED;
    }
    return walletHealth;
  }

  public static List<FundingRecord> adaptFundingHistory(
      List<BitgetDepositRecordsResponse> depositRecords) {
    return depositRecords.stream()
        .map(
            depHis ->
                new FundingRecord.Builder()
                    .setAddress(depHis.getToAddress())
                    .setCurrency(new Currency(depHis.getCoin()))
                    .setDate(depHis.getCTime())
                    .setAmount(depHis.getSize())
                    .setType(
                        depHis.getType().equalsIgnoreCase(DEPOSIT)
                            ? FundingRecord.Type.DEPOSIT
                            : FundingRecord.Type.WITHDRAWAL)
                    .setStatus(adaptDepositStatus(depHis.getStatus()))
                    .build())
        .collect(Collectors.toList());
  }

  public static List<Order> adaptOrder(
      CurrencyPair currencyPair, List<BitgetOrderHistoryResponse> bitgetOrderHistory) {
    List<Order> orderHistory = new ArrayList<>();
    for (BitgetOrderHistoryResponse bitgetOrderHistoryResponse : bitgetOrderHistory) {

      FeeDetailItem feeDetailItem =
          (FeeDetailItem)
              bitgetOrderHistoryResponse.getFeeDetail().getFeeDetails().values().toArray()[0];
      boolean isFeeUSDT =
          Currency.USDT.getCurrencyCode().equalsIgnoreCase(feeDetailItem.getFeeCoinCode());
      BigDecimal usdtFee;
      BigDecimal fee = null;

      // if newFees is not empty, calculate fees from newFees, but sometimes it can be empty, so we
      // need to use old fee data
      if (bitgetOrderHistoryResponse.getFeeDetail().getNewFees() != null) {
        if (isFeeUSDT) {
          usdtFee = bitgetOrderHistoryResponse.getFeeDetail().getNewFees().getT().abs();
        } else {
          usdtFee =
              bitgetOrderHistoryResponse
                  .getFeeDetail()
                  .getNewFees()
                  .getT()
                  .multiply(bitgetOrderHistoryResponse.getPriceAvg())
                  .abs();
          fee = bitgetOrderHistoryResponse.getFeeDetail().getNewFees().getT();
        }
      } else {
        if (isFeeUSDT) {
          usdtFee = feeDetailItem.getTotalFee().abs();
        } else {
          usdtFee =
              feeDetailItem.getTotalFee().multiply(bitgetOrderHistoryResponse.getPriceAvg()).abs();
          fee = feeDetailItem.getTotalFee();
        }
      }

      orderHistory.add(
          new LimitOrder.Builder(
                  BitgetAdapter.convertFromBitgetSide(bitgetOrderHistoryResponse.getSide()),
                  currencyPair)
              .originalAmount(bitgetOrderHistoryResponse.getSize())
              .cumulativeAmount(
                  isFeeUSDT
                      ? bitgetOrderHistoryResponse.getSize()
                      : bitgetOrderHistoryResponse.getSize().add(fee))
              .id(String.valueOf(bitgetOrderHistoryResponse.getOrderId()))
              .limitPrice(bitgetOrderHistoryResponse.getPrice())
              .averagePrice(bitgetOrderHistoryResponse.getPriceAvg())
              .orderStatus(BitgetAdapter.adaptOrderStatus(bitgetOrderHistoryResponse.getStatus()))
              .fee(usdtFee)
              .build());
    }
    return orderHistory;
  }

  private static Order.OrderStatus adaptOrderStatus(String orderStatus) {
    if (orderStatus == null) {
      return null;
    }
    switch (orderStatus) {
      case "init":
      case "live":
        return PENDING_NEW;
      case "new":
        return NEW;
      case "partially_fill":
        return PARTIALLY_FILLED;
      case "filled":
        return FILLED;
      case "cancelled":
        return CANCELED;
      default:
        throw new ExchangeException("Not supported status: " + orderStatus);
    }
  }

  public static String convertToSymbol(Instrument instrument) {
    return instrument.getBase().getCurrencyCode().toUpperCase()
        + instrument.getCounter().getCurrencyCode().toUpperCase();
  }

  public static String convertToSide(Order.OrderType type) {
    return type == Order.OrderType.BID ? BUY : SELL;
  }

  private static Order.OrderType convertFromBitgetSide(String side) {
    return side.equalsIgnoreCase(BUY) ? Order.OrderType.BID : Order.OrderType.ASK;
  }

  private static FundingRecord.Status adaptDepositStatus(String status) {
    if (status == null || status.isEmpty()) {
      return null;
    }
    switch (status) {
      case "fail":
        return FundingRecord.Status.FAILED;
      case "pending":
        return FundingRecord.Status.PROCESSING;
      case "success":
        return FundingRecord.Status.COMPLETE;
      default:
        throw new ExchangeException("Not supported deposit status: " + status);
    }
  }

  public static CandleStickData adaptCandleStickData(
      CurrencyPair currencyPair, List<String[]> bitgetCandles) {
    CandleStickData candleStickData = null;
    if (!bitgetCandles.isEmpty()) {
      List<CandleStick> candleStickList = new ArrayList<>();
      bitgetCandles.forEach(
          data ->
              candleStickList.add(
                  new CandleStick.Builder()
                      .timestamp(new Date(Long.parseLong(data[0])))
                      .open(new BigDecimal(data[1]))
                      .high(new BigDecimal(data[2]))
                      .low(new BigDecimal(data[3]))
                      .close(new BigDecimal(data[4]))
                      .volume(new BigDecimal(data[5]))
                      .quotaVolume(new BigDecimal(data[6]))
                      .build()));
      candleStickData = new CandleStickData(currencyPair, candleStickList);
    }
    return candleStickData;
  }

  public static OrderBook adaptOrderBook(
      CurrencyPair currencyPair, BitgetOrderBookResponse bitgetOrderBook) {
    Date ts = bitgetOrderBook.getTs();
    List<LimitOrder> asks =
        bitgetOrderBook.getAsks().stream()
            .map(PriceAndSize::new)
            .sorted(Ordering.natural().onResultOf((PriceAndSize s) -> s.price))
            .map(s -> adaptLimitOrder(currencyPair, ASK, s, ts))
            .collect(toCollection(LinkedList::new));
    List<LimitOrder> bids =
        bitgetOrderBook.getBids().stream()
            .map(PriceAndSize::new)
            .sorted(Ordering.natural().onResultOf((PriceAndSize s) -> s.price).reversed())
            .map(s -> adaptLimitOrder(currencyPair, BID, s, ts))
            .collect(toCollection(LinkedList::new));

    return new OrderBook(ts, asks, bids);
  }

  private static final class PriceAndSize {
    final BigDecimal price;
    final BigDecimal size;

    PriceAndSize(List<String> item) {
      this.price = new BigDecimal(item.get(0));
      this.size = new BigDecimal(item.get(1));
    }
  }

  private static LimitOrder adaptLimitOrder(
      CurrencyPair currencyPair,
      Order.OrderType orderType,
      PriceAndSize priceAndSize,
      Date timestamp) {
    return new LimitOrder.Builder(orderType, currencyPair)
        .limitPrice(priceAndSize.price)
        .originalAmount(priceAndSize.size)
        .orderStatus(NEW)
        .timestamp(timestamp)
        .build();
  }
}
