package org.knowm.xchange.probit;

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
import org.knowm.xchange.probit.dto.response.*;
import org.knowm.xchange.probit.model.ProbitStatus;
import org.knowm.xchange.probit.model.ProbitTransferType;

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

public class ProbitAdapter {

  private static final SimpleDateFormat DATE_FORMATTER =
      new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

  private static final String ORDER_TYPE_SELL = "sell";
  private static final String ORDER_TYPE_BUY = "buy";
  private static final String DEFAULT_TRADE_FEE = "0.002";

  private ProbitAdapter() {
    // hides the public one
  }

  public static Wallet adaptWallet(List<ProbitBalanceDTO> probitBalanceDTOS) {
    List<Balance> balances = new ArrayList<>();
    for (ProbitBalanceDTO probitBalanceDTO : probitBalanceDTOS) {
      Currency currency = new Currency(probitBalanceDTO.getCurrencyId());
      BigDecimal lockedBalance =
          probitBalanceDTO.getTotal().subtract(probitBalanceDTO.getAvailable());
      balances.add(
          new Balance(
              currency,
              probitBalanceDTO.getTotal(),
              probitBalanceDTO.getAvailable(),
              lockedBalance));
    }

    return Wallet.Builder.from(balances).build();
  }

  public static String formatDate(Date date) {
    return date != null ? DATE_FORMATTER.format(date) : null;
  }

  public static List<FundingRecord> adaptTransferHistory(List<ProbitTransferDTO> probitTransfers) {
    return probitTransfers.stream()
        .map(
            transfer ->
                new FundingRecord.Builder()
                    .setAddress(transfer.getAddress())
                    .setAddressTag(transfer.getDestinationTag())
                    .setCurrency(new Currency(transfer.getCurrencyId()))
                    .setDate(transfer.getTime())
                    .setBlockchainTransactionHash(transfer.getHash())
                    .setAmount(transfer.getAmount())
                    .setType(
                        adaptTransferType(
                            ProbitTransferType.valueOf(transfer.getType().toUpperCase())))
                    .setStatus(
                        adaptTransferStatus(
                            ProbitStatus.valueOf(transfer.getStatus().toUpperCase())))
                    .build())
        .collect(Collectors.toList());
  }

  public static ExchangeMetaData adaptToExchangeMetaData(
      ExchangeMetaData exchangeMetaData,
      List<ProbitMarketDTO> marketData,
      List<ProbitCurrencyDTO> currencyData) {
    Map<Instrument, InstrumentMetaData> pairs =
        marketData.stream()
            .collect(
                Collectors.toMap(
                    marketDatum -> adaptFromProbitSymbol(marketDatum.getId()),
                    ProbitAdapter::adaptCurrencyMetaData,
                    (a, b) -> b));

    Map<Currency, CurrencyMetaData> currencies = new HashMap<>();
    for (ProbitCurrencyDTO currencyDatum : currencyData) {
      CurrencyMetaData currencyMetaData =
          new CurrencyMetaData(
              currencyDatum.getPrecision(),
              currencyDatum.getWithdrawalFee(),
              currencyDatum.getMinWithdrawalAmount(),
              getWalletHealthStatus(currencyDatum));
      currencies.put(new Currency(currencyDatum.getId()), currencyMetaData);
    }

    return new ExchangeMetaData(
        pairs,
        currencies,
        exchangeMetaData.getPublicRateLimits(),
        exchangeMetaData.getPrivateRateLimits(),
        true);
  }

  private static WalletHealth getWalletHealthStatus(ProbitCurrencyDTO currencyDatum) {
    WalletHealth walletHealth = WalletHealth.ONLINE;
    if (currencyDatum.getDepositSuspended() && currencyDatum.getWithdrawalSuspended()) {
      walletHealth = WalletHealth.OFFLINE;
    } else if (currencyDatum.getDepositSuspended()) {
      walletHealth = WalletHealth.DEPOSITS_DISABLED;
    } else if (currencyDatum.getWithdrawalSuspended()) {
      walletHealth = WalletHealth.WITHDRAWALS_DISABLED;
    }
    return walletHealth;
  }

  private static InstrumentMetaData adaptCurrencyMetaData(ProbitMarketDTO marketDatum) {
    return new InstrumentMetaData.Builder()
        .tradingFee(marketDatum.getTakerFeeRate())
        .minimumAmount(marketDatum.getMinQuantity())
        .maximumAmount(marketDatum.getMaxQuantity())
        .amountStepSize(new BigDecimal("1").movePointLeft(marketDatum.getQuantityPrecision()))
        .volumeScale(marketDatum.getMinQuantity().scale())
        .build();
  }

  private static FundingRecord.Status adaptTransferStatus(ProbitStatus probitStatus) {
    if (probitStatus == null) {
      return null;
    }
    switch (probitStatus) {
      case APPLYING:
      case CONFIRMING:
      case CONFIRMED:
      case PENDING:
      case REQUESTED:
        return FundingRecord.Status.PROCESSING;
      case FAILED:
      case CANCELLED:
      case CANCELLING:
        return FundingRecord.Status.CANCELLED;
      case DONE:
        return FundingRecord.Status.COMPLETE;
      default:
        throw new ExchangeException("Not supported status: " + probitStatus);
    }
  }

  private static FundingRecord.Type adaptTransferType(ProbitTransferType probitTransferType) {
    if (probitTransferType == ProbitTransferType.DEPOSIT) {
      return FundingRecord.Type.DEPOSIT;
    } else if (probitTransferType == ProbitTransferType.WITHDRAWAL) {
      return FundingRecord.Type.WITHDRAWAL;
    }

    return null;
  }

  public static CurrencyPair adaptFromProbitSymbol(String probitSymbol) {
    String[] splitSymbol = probitSymbol.split("-");
    return new CurrencyPair(splitSymbol[0], splitSymbol[1]);
  }

  public static Map<Instrument, Boolean> adaptSupportedInstruments(
      List<ProbitMarketDTO> marketData) {
    Map<Instrument, Boolean> supportedInstrumentMap = new HashMap<>();
    for (ProbitMarketDTO marketDatum : marketData) {
      CurrencyPair currencyPair = adaptFromProbitSymbol(marketDatum.getId());
      if (marketDatum.getShowInUI() && !marketDatum.getClosed()) {
        supportedInstrumentMap.put(currencyPair, true);
      } else {
        supportedInstrumentMap.put(currencyPair, false);
      }
    }

    return supportedInstrumentMap;
  }

  public static List<DepositAddress> adaptDepositAddresses(
      List<ProbitDepositAddressDTO> probitDepositAddressDTOS) {
    return probitDepositAddressDTOS.stream()
        .map(
            depositAddressDTO ->
                new DepositAddress(
                    depositAddressDTO.getCurrencyId(),
                    depositAddressDTO.getAddress(),
                    depositAddressDTO.getAddressTag(),
                    null))
        .collect(Collectors.toList());
  }

  public static Map<Currency, String> getCurrencyPlatforms(
      List<ProbitCurrencyDTO> probitCurrencyDTOS) {
    return probitCurrencyDTOS.stream()
        .collect(
            Collectors.toMap(
                probitCurrencyDTO -> new Currency(probitCurrencyDTO.getId()),
                ProbitCurrencyDTO::getPlatform,
                (a, b) -> b));
  }

  public static ProbitTransferType adaptTransferType(FundingRecord.Type fundingRecordType) {

    if (ProbitTransferType.WITHDRAWAL.name().equalsIgnoreCase(fundingRecordType.name())) {
      return ProbitTransferType.WITHDRAWAL;
    } else if (ProbitTransferType.DEPOSIT.name().equalsIgnoreCase(fundingRecordType.name())) {
      return ProbitTransferType.DEPOSIT;
    }

    return null;
  }

  public static OrderBook adaptOrderBook(
      CurrencyPair currencyPair, List<ProbitOrderBookDTO> probitOrderBookDTOS) {
    Date timeStamp = new Date();
    List<LimitOrder> asks =
        probitOrderBookDTOS.stream()
            .filter(ob -> ob.getSide().equals(ORDER_TYPE_SELL))
            .sorted(Ordering.natural().onResultOf(ProbitOrderBookDTO::getPrice))
            .map(s -> adaptLimitOrder(currencyPair, ASK, s.getPrice(), s.getQuantity(), timeStamp))
            .collect(toCollection(LinkedList::new));
    List<LimitOrder> bids =
        probitOrderBookDTOS.stream()
            .filter(ob -> ob.getSide().equals(ORDER_TYPE_BUY))
            .sorted(Ordering.natural().onResultOf(ProbitOrderBookDTO::getPrice).reversed())
            .map(s -> adaptLimitOrder(currencyPair, BID, s.getPrice(), s.getQuantity(), timeStamp))
            .collect(toCollection(LinkedList::new));
    return new OrderBook(timeStamp, asks, bids);
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

  public static CandleStickData adaptCandleStickData(
      CurrencyPair currencyPair, List<ProbitCandleDTO> probitCandleDTOS) {
    CandleStickData candleStickData = null;
    if (!probitCandleDTOS.isEmpty()) {
      List<CandleStick> candleStickList = new ArrayList<>();
      probitCandleDTOS.forEach(
          data ->
              candleStickList.add(
                  new CandleStick.Builder()
                      .timestamp(data.getStartTime())
                      .open(data.getOpen())
                      .high(data.getHigh())
                      .low(data.getLow())
                      .close(data.getClose())
                      .quotaVolume(data.getQuoteVolume())
                      .volume(data.getBaseVolume())
                      .build()));
      candleStickData = new CandleStickData(currencyPair, candleStickList);
    }

    return candleStickData;
  }

  public static String adaptToProbitSide(Order.OrderType type) {
    return type == ASK ? ORDER_TYPE_SELL : ORDER_TYPE_BUY;
  }

  public static Order.OrderType adaptFromProbitSide(String probitSide) {
    return probitSide.equalsIgnoreCase(ORDER_TYPE_SELL) ? ASK : BID;
  }

  public static Order adaptOrder(ProbitLimitOrderDTO probitLimitOrderDTO, BigDecimal tradingFee) {
    BigDecimal averagePrice =
            probitLimitOrderDTO.getFilledCost().equals(BigDecimal.ZERO)
                    ? BigDecimal.ZERO
                    : BigDecimal.valueOf(
                            probitLimitOrderDTO.getFilledCost().doubleValue()
                                    / probitLimitOrderDTO.getFilledQuantity().doubleValue())
                    .setScale(probitLimitOrderDTO.getLimitPrice().scale(), RoundingMode.HALF_EVEN);

    BigDecimal usdtFee;
    if(tradingFee != null && tradingFee.doubleValue() == 0.0) {
      usdtFee = probitLimitOrderDTO.getFilledCost().multiply(tradingFee);
    } else {
      usdtFee = probitLimitOrderDTO.getFilledCost().multiply(new BigDecimal(DEFAULT_TRADE_FEE));
    }

    return new LimitOrder.Builder(
            ProbitAdapter.adaptFromProbitSide(probitLimitOrderDTO.getSide()),
            ProbitAdapter.adaptFromProbitSymbol(probitLimitOrderDTO.getMarketId()))
            .originalAmount(probitLimitOrderDTO.getQuantity())
            .cumulativeAmount(probitLimitOrderDTO.getFilledQuantity())
            .id(String.valueOf(probitLimitOrderDTO.getId()))
            .limitPrice(probitLimitOrderDTO.getLimitPrice())
            .averagePrice(averagePrice)
            .orderStatus(ProbitAdapter.adaptFromProbitOrderStatus(probitLimitOrderDTO.getStatus()))
            .fee(usdtFee)
            .build();
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
}
