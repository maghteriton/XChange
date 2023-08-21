package org.knowm.xchange.mexc;

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
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.dto.meta.WalletHealth;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.mexc.dto.account.*;
import org.knowm.xchange.mexc.dto.market.*;
import org.knowm.xchange.mexc.dto.trade.MEXCDeal;
import org.knowm.xchange.mexc.dto.trade.MEXCOrder;
import org.knowm.xchange.mexc.dto.trade.MEXCOrderRequestPayload;
import org.knowm.xchange.utils.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MEXCAdapters {

  private static final SimpleDateFormat DATE_FORMAT =
      new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

  private static final String SUCCESS = "success";

  public static Wallet adaptMEXCBalances(Map<String, MEXCBalance> mexcBalances) {
    List<Balance> balances = new ArrayList<>(mexcBalances.size());
    for (Map.Entry<String, MEXCBalance> mexcBalance : mexcBalances.entrySet()) {
      MEXCBalance mexcBalanceValue = mexcBalance.getValue();
      BigDecimal available = new BigDecimal(mexcBalanceValue.getAvailable());
      BigDecimal frozen = new BigDecimal(mexcBalanceValue.getFrozen());
      balances.add(
          new Balance(new Currency(mexcBalance.getKey()), frozen.add(available), available));
    }
    return Wallet.Builder.from(balances).build();
  }

  public static String convertToMEXCSymbol(CurrencyPair currencyPair) {
    return currencyPair.toString().replace("/", "_").toUpperCase();
  }

  public static Instrument adaptSymbol(String symbol) {
    String[] symbolTokenized = symbol.split("_");
    return new CurrencyPair(symbolTokenized[0], symbolTokenized[1]);
  }

  public static Order.OrderType adaptOrderType(String mexcOrderType) {
    Order.OrderType result = Order.OrderType.ASK;
    if (Order.OrderType.BID.toString().equals(mexcOrderType)) {
      result = Order.OrderType.BID;
    }
    return result;
  }

  public static InstrumentMetaData adaptPair(MEXCSymbols mexcSymbol) {
    return new InstrumentMetaData.Builder()
        .tradingFee(new BigDecimal(mexcSymbol.getTakerFeeRate()))
        .minimumAmount(new BigDecimal(mexcSymbol.getMinAmount()))
        .priceScale(mexcSymbol.getPriceScale())
        .volumeScale(mexcSymbol.getQuantityScale())
        .feeTiers(null)
        .marketOrderEnabled(mexcSymbol.getState().equals("ENABLED"))
        .build();
  }

  public static MEXCOrderRequestPayload adaptOrder(LimitOrder limitOrder) {
    return new MEXCOrderRequestPayload(
        convertToMEXCSymbol((CurrencyPair) limitOrder.getInstrument()),
        limitOrder.getLimitPrice().toPlainString(),
        limitOrder.getOriginalAmount().toPlainString(),
        limitOrder.getType().toString(),
        "LIMIT_ORDER",
        null);
  }

  public static Order adaptOrder(MEXCOrder mexcOrder) {

    BigDecimal dealQuantity = new BigDecimal(mexcOrder.getDealQuantity());
    LimitOrder limitOrder =
        new LimitOrder(
            Order.OrderType.valueOf(mexcOrder.getType()),
            new BigDecimal(mexcOrder.getQuantity()),
            dealQuantity,
            adaptSymbol(mexcOrder.getSymbol()),
            mexcOrder.getId(),
            new Date(mexcOrder.getCreateTime()),
            new BigDecimal(mexcOrder.getPrice())) {};
    BigDecimal dealAmount = new BigDecimal(mexcOrder.getDealAmount());
    BigDecimal averagePrice = getAveragePrice(dealQuantity, dealAmount);
    limitOrder.setAveragePrice(averagePrice);
    limitOrder.setOrderStatus(Order.OrderStatus.valueOf(mexcOrder.getState()));
    return limitOrder;
  }

  private static BigDecimal getAveragePrice(BigDecimal dealQuantity, BigDecimal dealAmount) {
    if (dealQuantity.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    }
    return dealAmount.divide(dealQuantity, RoundingMode.HALF_EVEN);
  }

  public static ExchangeMetaData adaptToExchangeMetaData(
      List<MEXCSymbols> mexcSymbols, List<MEXCCurrencyInfo> coinList) {
    Map<Instrument, InstrumentMetaData> pairs = new HashMap<>();
    for (MEXCSymbols mexcSymbol : mexcSymbols) {
      if (!mexcSymbol.getLimited()) {
        CurrencyPair currencyPair = (CurrencyPair) MEXCAdapters.adaptSymbol(mexcSymbol.getSymbol());
        pairs.put(currencyPair, adaptPair(mexcSymbol));
      }
    }

    Map<Currency, CurrencyMetaData> currencies = new HashMap<>();
    for (MEXCCurrencyInfo mexcCurrencyInfo : coinList) {
      List<MEXCCurrency> mexcCoinList = mexcCurrencyInfo.getMexcCoinList();
      if (!mexcCoinList.isEmpty()) {
        String currency = mexcCurrencyInfo.getCurrency();
        MEXCCurrency mexcCurrency = mexcCoinList.get(0);
        CurrencyMetaData currencyMetaData =
            new CurrencyMetaData(
                mexcCurrency.getPrecision().intValue(),
                mexcCurrency.getWithdrawalFee(),
                mexcCurrency.getWithdrawLimitMin(),
                getWalletHealthStatus(mexcCurrency));
        currencies.put(new Currency(currency), currencyMetaData);
      }
    }

    return new ExchangeMetaData(pairs, currencies, null, null, null);
  }

  private static WalletHealth getWalletHealthStatus(MEXCCurrency mexcCurrency) {
    WalletHealth walletHealth = WalletHealth.ONLINE;
    if (!mexcCurrency.getIsDepositEnabled() && !mexcCurrency.getIsWithdrawEnabled()) {
      walletHealth = WalletHealth.OFFLINE;
    } else if (!mexcCurrency.getIsDepositEnabled()) {
      walletHealth = WalletHealth.DEPOSITS_DISABLED;
    } else if (!mexcCurrency.getIsWithdrawEnabled()) {
      walletHealth = WalletHealth.WITHDRAWALS_DISABLED;
    }
    return walletHealth;
  }

  public static List<FundingRecord> adaptDepositWithdrawLists(
      MEXCAssetTransferHistory<MEXCDepositRecord> depositHistory,
      MEXCAssetTransferHistory<MEXCWithdrawalRecord> withdrawHistory) {
    List<FundingRecord> result = new ArrayList<>();

    depositHistory
        .getRecordList()
        .forEach(
            mexcRecord -> {
              FundingRecord fundingRecord =
                  new FundingRecord(
                      mexcRecord.getAddress(),
                      convertToDate(mexcRecord.getUpdateTime()),
                      Currency.getInstance(mexcRecord.getCurrency()),
                      mexcRecord.getAmount(),
                      mexcRecord.getTxId(),
                      mexcRecord.getTransHash(),
                      FundingRecord.Type.DEPOSIT,
                      getStatus(mexcRecord.getState()),
                      null,
                      mexcRecord.getFee(),
                      null);
              result.add(fundingRecord);
            });

    withdrawHistory
        .getRecordList()
        .forEach(
            mexcRecord -> {
              FundingRecord fundingRecord =
                  new FundingRecord(
                      mexcRecord.getAddress(),
                      convertToDate(mexcRecord.getUpdateTime()),
                      Currency.getInstance(mexcRecord.getCurrency()),
                      mexcRecord.getAmount(),
                      mexcRecord.getId(),
                      mexcRecord.getTxId(),
                      FundingRecord.Type.WITHDRAWAL,
                      getStatus(mexcRecord.getState()),
                      null,
                      mexcRecord.getFee(),
                      null);
              result.add(fundingRecord);
            });

    return result;
  }

  private static FundingRecord.Status getStatus(String gateioStatus) {
    switch (gateioStatus) {
      case "SUCCESS":
        return FundingRecord.Status.COMPLETE;
      case "CANCEL":
        return FundingRecord.Status.CANCELLED;
      default:
        return FundingRecord.Status.PROCESSING;
    }
  }

  private static Date convertToDate(String dateString) {
    Date date;
    try {
      date = DATE_FORMAT.parse(dateString);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    return date;
  }

  public static List<DepositAddress> adaptDepositAddresses(MEXCDepositAddress mexcDepositAddress) {
    List<DepositAddress> depositAddresses = new ArrayList<>();
    if(!mexcDepositAddress.getMexcChainList().isEmpty()) {
      mexcDepositAddress
              .getMexcChainList()
              .forEach(
                      mexcChain -> {
                        DepositAddress depositAddress =
                                new DepositAddress(
                                        mexcDepositAddress.getCurrency(),
                                        mexcChain.getRealAddress(),
                                        mexcChain.getMemo(),
                                        mexcChain.getChain());
                        depositAddresses.add(depositAddress);
                      });
    }
    return depositAddresses;
  }

  public static OrderBook adaptDepth(CurrencyPair currencyPair, MEXCDepth mexcDepth) {
    List<LimitOrder> bids = new ArrayList<>();
    mexcDepth
        .getBids()
        .forEach(
            e ->
                bids.add(
                    new LimitOrder(
                        Order.OrderType.BID,
                        e.getQuantity(),
                        currencyPair,
                        null,
                        null,
                        e.getPrice())));

    List<LimitOrder> asks = new ArrayList<>();
    mexcDepth
        .getAsks()
        .forEach(
            e ->
                asks.add(
                    new LimitOrder(
                        Order.OrderType.ASK,
                        e.getQuantity(),
                        currencyPair,
                        null,
                        null,
                        e.getPrice())));

    return new OrderBook(new Date(), asks, bids);
  }

  public static CandleStickData adaptCandleStickData(
      List<MEXCCandleData> mexcCandleStickData, CurrencyPair currencyPair) {
    CandleStickData candleStickData = null;
    if (!mexcCandleStickData.isEmpty()) {
      List<CandleStick> candleStickList = new ArrayList<>();
      mexcCandleStickData.forEach(
          data ->
              candleStickList.add(
                  new CandleStick.Builder()
                      .timestamp(new Date(data.getTime() * 1000))
                      .open(data.getOpen())
                      .high(data.getHigh())
                      .low(data.getLow())
                      .close(data.getClose())
                      .volume(data.getVol())
                      .build()));
      candleStickData = new CandleStickData(currencyPair, candleStickList);
    }

    return candleStickData;
  }

  public static UserTrades adaptUserTrades(List<MEXCDeal> dealHistory) {

    List<UserTrade> tradeList = new ArrayList<>();
    dealHistory.forEach(deal -> tradeList.add(adaptTrade(deal)));

    return new UserTrades(tradeList, Trades.TradeSortType.SortByTimestamp);
  }

  public static Boolean adaptCancelOrder(Map<String, String> cancelOrderMap, String orderId) {
    return SUCCESS.equalsIgnoreCase(cancelOrderMap.get(orderId));
  }

  private static UserTrade adaptTrade(MEXCDeal deal) {
    Date timestamp = DateUtils.fromMillisUtc(deal.getCreateTime());
    Order.OrderType type =
        deal.getTradeType().equals(Order.OrderType.BID.name())
            ? Order.OrderType.BID
            : Order.OrderType.ASK;

    return new UserTrade.Builder()
        .type(type)
        .originalAmount(new BigDecimal(deal.getAmount()))
        .currencyPair((CurrencyPair) adaptSymbol(deal.getSymbol()))
        .price(new BigDecimal(deal.getPrice()))
        .timestamp(timestamp)
        .id(deal.getId())
        .orderId(deal.getOrderId())
        .feeAmount(new BigDecimal(deal.getFee()))
        .feeCurrency(new Currency(deal.getFeeCurrency()))
        .build();
  }
}
