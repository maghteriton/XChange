package org.knowm.xchange.bingx;

import com.google.common.collect.Ordering;
import org.knowm.xchange.bingx.dto.*;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.Order.OrderStatus;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.DepositAddress;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.account.FundingRecord.Status;
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
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;
import static org.knowm.xchange.dto.Order.OrderStatus.*;
import static org.knowm.xchange.dto.Order.OrderType.ASK;
import static org.knowm.xchange.dto.Order.OrderType.BID;

public class BingxAdapter {

  public static final int ONLINE = 1;

  public static List<DepositAddress> adaptDepositAddresses(
      List<BingxDepositAddressesDTO> depositAddresses) {
    return depositAddresses.stream()
        .map(
            depAddress ->
                new DepositAddress(
                    depAddress.getCoin(),
                    depAddress.getAddress(),
                    depAddress.getTag(),
                    depAddress.getNetwork()))
        .collect(Collectors.toList());
  }

  public static List<FundingRecord> adaptFundingHistory(List<BingxDepositDTO> depositHistory) {
    return depositHistory.stream()
        .map(
            depHis ->
                new FundingRecord.Builder()
                    .setAddress(depHis.getAddress())
                    .setAddressTag(depHis.getAddressTag())
                    .setCurrency(new Currency(depHis.getCoin()))
                    .setDate(depHis.getInsertTime())
                    .setBlockchainTransactionHash(depHis.getTxId())
                    .setAmount(depHis.getAmount())
                    .setStatus(adaptDepositStatus(depHis.getStatus()))
                    .build())
        .collect(Collectors.toList());
  }

  public static Wallet adaptWallet(List<BingxBalanceDTO> bingxBalances) {
    List<Balance> balances = new ArrayList<>();
    for (BingxBalanceDTO bingxBalance : bingxBalances) {
      Currency currency = new Currency(bingxBalance.getAsset());
      BigDecimal freeBalance = new BigDecimal(bingxBalance.getFree());
      BigDecimal lockedBalance = new BigDecimal(bingxBalance.getLocked());
      balances.add(
          new Balance(currency, freeBalance.add(lockedBalance), freeBalance, lockedBalance));
    }

    return Wallet.Builder.from(balances).build();
  }

  public static CandleStickData adaptCandleStickData(
      CurrencyPair currencyPair, List<List<String>> kLineData) {
    CandleStickData candleStickData = null;
    if (!kLineData.isEmpty()) {
      List<CandleStick> candleStickList = new ArrayList<>();
      kLineData.forEach(
          data ->
              candleStickList.add(
                  new CandleStick.Builder()
                      .timestamp(new Date(Long.parseLong(data.get(0))))
                      .open(new BigDecimal(data.get(1)))
                      .high(new BigDecimal(data.get(2)))
                      .low(new BigDecimal(data.get(3)))
                      .close(new BigDecimal(data.get(4)))
                      .quotaVolume(new BigDecimal(data.get(5)))
                      .volume(new BigDecimal(7))
                      .build()));
      candleStickData = new CandleStickData(currencyPair, candleStickList);
    }

    return candleStickData;
  }

  public static OrderBook adaptOrderBook(
      CurrencyPair currencyPair, BingxMarketDepthDTO marketDepth) {
    List<LimitOrder> asks =
        marketDepth.getAsks().stream()
            .map(PriceAndSize::new)
            .sorted(Ordering.natural().onResultOf((PriceAndSize s) -> s.price).reversed())
            .map(s -> adaptLimitOrder(currencyPair, ASK, s, marketDepth.getTs()))
            .collect(toCollection(LinkedList::new));
    List<LimitOrder> bids =
        marketDepth.getBids().stream()
            .map(PriceAndSize::new)
            .sorted(Ordering.natural().onResultOf((PriceAndSize s) -> s.price))
            .map(s -> adaptLimitOrder(currencyPair, BID, s, marketDepth.getTs()))
            .collect(toCollection(LinkedList::new));

    return new OrderBook(marketDepth.getTs(), asks, bids);
  }

  private static LimitOrder adaptLimitOrder(
      CurrencyPair currencyPair, OrderType orderType, PriceAndSize priceAndSize, Date timestamp) {
    return new LimitOrder.Builder(orderType, currencyPair)
        .limitPrice(priceAndSize.price)
        .originalAmount(priceAndSize.size)
        .orderStatus(NEW)
        .timestamp(timestamp)
        .build();
  }

  public static String adaptToBingxSymbol(CurrencyPair instrument) {
    return instrument.getBase() + "-" + instrument.getCounter();
  }

  public static CurrencyPair adaptFromBingxSymbol(String bingxSymbol) {
    String[] splitSymbol = bingxSymbol.split("-");
    return new CurrencyPair(splitSymbol[0], splitSymbol[1]);
  }

  public static Order adaptOrder(BingxOrderDTO bingxOrderDTO) {
    BigDecimal averagePrice =
        bingxOrderDTO.getExecutedQty().equals(BigDecimal.ZERO)
            ? BigDecimal.ZERO
            : bingxOrderDTO
                .getCummulativeQuoteQty()
                .divide(bingxOrderDTO.getExecutedQty(), RoundingMode.HALF_EVEN)
                .setScale(bingxOrderDTO.getPrice().scale(), RoundingMode.HALF_EVEN);

    return new LimitOrder.Builder(
            BingxAdapter.adaptFromBingxSide(bingxOrderDTO.getSide()),
            BingxAdapter.adaptFromBingxSymbol(bingxOrderDTO.getSymbol()))
        .originalAmount(bingxOrderDTO.getOrigQty())
        .cumulativeAmount(bingxOrderDTO.getExecutedQty())
        .remainingAmount(bingxOrderDTO.getOrigQty().subtract(bingxOrderDTO.getExecutedQty()))
        .id(String.valueOf(bingxOrderDTO.getOrderId()))
        .limitPrice(bingxOrderDTO.getPrice())
        .averagePrice(averagePrice)
        .orderStatus(BingxAdapter.adaptOrderStatus(bingxOrderDTO.getStatus()))
        .fee(bingxOrderDTO.getFee())
        .build();
  }

  public static ExchangeMetaData adaptToExchangeMetaData(
      ExchangeMetaData exchangeMetaData,
      List<BingxSymbolDTO> symbols,
      List<BingxWalletDTO> wallets,
      TradeCommissionRateDTO commissionRate) {
    Map<Instrument, InstrumentMetaData> pairs = new HashMap<>();
    for (BingxSymbolDTO bingxSymbol : symbols) {
      if (bingxSymbol.getStatus() == ONLINE
          && bingxSymbol.isApiStateBuy()
          && bingxSymbol.isApiStateSell()) {
        CurrencyPair currencyPair = BingxAdapter.adaptFromBingxSymbol(bingxSymbol.getSymbol());
        pairs.put(currencyPair, BingxAdapter.adaptCurrencyMetaData(bingxSymbol, commissionRate));
      }
    }

    Map<Currency, CurrencyMetaData> currencies = new HashMap<>();
    for (BingxWalletDTO wallet : wallets) {

      BingxNetworkDTO bingxNetwork = wallet.getNetworkList().get(0);
      CurrencyMetaData currencyMetaData =
          new CurrencyMetaData(
              bingxNetwork.getDepositMin().scale(),
              bingxNetwork.getWithdrawFee(),
              bingxNetwork.getWithdrawMin(),
              BingxAdapter.getWalletHealthStatus(bingxNetwork));
      currencies.put(new Currency(wallet.getCoin()), currencyMetaData);
    }

    return new ExchangeMetaData(
        pairs,
        currencies,
        exchangeMetaData.getPublicRateLimits(),
        exchangeMetaData.getPrivateRateLimits(),
        true);
  }

  private static InstrumentMetaData adaptCurrencyMetaData(
      BingxSymbolDTO bingxSymbol, TradeCommissionRateDTO commissionRate) {
    return new InstrumentMetaData.Builder()
        .tradingFee(commissionRate.getTakerCommissionRate())
        .minimumAmount(bingxSymbol.getMinimumQuantity())
        .maximumAmount(bingxSymbol.getMaximumQuantity())
        .amountStepSize(bingxSymbol.getStepSize())
        .volumeScale(bingxSymbol.getMinimumQuantity().scale())
        .build();
  }

  public static String adaptToBingxSide(OrderType type) {
    return type == ASK ? "SELL" : "BUY";
  }

  public static OrderType adaptFromBingxSide(String bingxSide) {
    return bingxSide.equalsIgnoreCase("SELL") ? ASK : BID;
  }

  private static OrderStatus adaptOrderStatus(String orderStatus) {
    if (orderStatus == null) {
      return null;
    }
    switch (orderStatus) {
      case "NEW":
        return NEW;
      case "PENDING":
        return PENDING_NEW;
      case "PARTIALLY_FILLED":
        return PARTIALLY_FILLED;
      case "FILLED":
        return FILLED;
      case "CANCELED":
        return CANCELED;
      case "FAILED":
        return REJECTED;
      default:
        throw new ExchangeException("Not supported status: " + orderStatus);
    }
  }

  private static Status adaptDepositStatus(Integer depHis) {
    if (depHis == null) {
      return null;
    }
    switch (depHis) {
      case 0:
      case 6:
        return Status.PROCESSING;
      case 1:
        return Status.COMPLETE;
      default:
        throw new ExchangeException("Not supported status: " + depHis);
    }
  }

  private static WalletHealth getWalletHealthStatus(BingxNetworkDTO bingxNetwork) {
    WalletHealth walletHealth = WalletHealth.ONLINE;
    if (!bingxNetwork.isDepositEnable() && !bingxNetwork.isWithdrawEnable()) {
      walletHealth = WalletHealth.OFFLINE;
    } else if (!bingxNetwork.isDepositEnable()) {
      walletHealth = WalletHealth.DEPOSITS_DISABLED;
    } else if (!bingxNetwork.isWithdrawEnable()) {
      walletHealth = WalletHealth.WITHDRAWALS_DISABLED;
    }
    return walletHealth;
  }

  private static final class PriceAndSize {
    final BigDecimal price;
    final BigDecimal size;

    PriceAndSize(List<String> data) {
      this.price = new BigDecimal(data.get(0));
      this.size = new BigDecimal(data.get(1));
    }
  }
}
