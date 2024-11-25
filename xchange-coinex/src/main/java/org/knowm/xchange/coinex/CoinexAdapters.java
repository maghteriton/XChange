package org.knowm.xchange.coinex;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import org.knowm.xchange.coinex.dto.account.*;
import org.knowm.xchange.coinex.dto.market.CoinexKlineData;
import org.knowm.xchange.coinex.dto.market.CoinexMarketDepth;
import org.knowm.xchange.coinex.dto.market.CoinexMarketInfo;
import org.knowm.xchange.coinex.dto.trade.CoinexCancelOrderRequest;
import org.knowm.xchange.coinex.dto.trade.CoinexOrderRequestPayload;
import org.knowm.xchange.coinex.dto.trade.CoinexOrderResponse;
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
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.trade.params.DefaultWithdrawFundsParams;

public class CoinexAdapters {

    public static Wallet adaptWallet(Map<String, CoinexBalanceInfo> coinexBalances) {
        List<Balance> balances = new ArrayList<>(coinexBalances.size());
        for (Map.Entry<String, CoinexBalanceInfo> balancePair : coinexBalances.entrySet()) {
            Currency currency = new Currency(balancePair.getKey());
            BigDecimal total =
                    balancePair.getValue().getAvailable().add(balancePair.getValue().getFrozen());
            Balance balance =
                    new Balance(
                            currency,
                            total,
                            balancePair.getValue().getAvailable(),
                            balancePair.getValue().getFrozen());
            balances.add(balance);
        }

        return Wallet.Builder.from(balances).build();
    }

    public static ExchangeMetaData adaptToExchangeMetaData(
            List<CoinexMarketInfo> coinexMarketInfoList,
            List<CoinexAssetDetail> coinexAssetList) {
        Map<Instrument, InstrumentMetaData> pairs = new HashMap<>();
        for (CoinexMarketInfo marketInfo : coinexMarketInfoList) {
            if (marketInfo.getQuoteCcy().equalsIgnoreCase(Currency.USDT.getCurrencyCode())) {
                CurrencyPair currencyPair = (CurrencyPair) adaptSymbol(marketInfo);
                pairs.put(currencyPair, adaptPair(marketInfo));
            }
        }

        Map<Currency, CurrencyMetaData> currencies = new HashMap<>();
        for (CoinexAssetDetail coinexAssetDetail : coinexAssetList) {
            String currency = coinexAssetDetail.getAsset().getCcy();
            if(!coinexAssetDetail.getChains().isEmpty()) {
                CoinexAssetDetail.Chain chain = coinexAssetDetail.getChains().get(0);
                CurrencyMetaData currencyMetaData =
                        new CurrencyMetaData(
                                chain.getWithdrawalPrecision(),
                                chain.getWithdrawalFee(),
                                chain.getMinWithdrawAmount(),
                                getWalletHealthStatus(chain));
                currencies.put(new Currency(currency), currencyMetaData);
            }
        }

        return new ExchangeMetaData(pairs, currencies, null, null, null);
    }

    private static WalletHealth getWalletHealthStatus(CoinexAssetDetail.Chain coinexChain) {
        WalletHealth walletHealth = WalletHealth.ONLINE;
        if (!coinexChain.isDepositEnabled() && !coinexChain.isWithdrawEnabled()) {
            walletHealth = WalletHealth.OFFLINE;
        } else if (!coinexChain.isDepositEnabled()) {
            walletHealth = WalletHealth.DEPOSITS_DISABLED;
        } else if (!coinexChain.isWithdrawEnabled()) {
            walletHealth = WalletHealth.WITHDRAWALS_DISABLED;
        }
        return walletHealth;
    }

    public static String getCcy(Currency currency) {
        return currency != null ? currency.getCurrencyCode().toUpperCase() : null;
    }

    public static String getMarket(CurrencyPair currencyPair) {
        return String.format("%s%s",
                currencyPair.getBase().getCurrencyCode().toUpperCase(),
                currencyPair.getCounter().getCurrencyCode().toUpperCase());
    }

    public static CurrencyPair getMarket(String currencyPairString, String counter) {
        String[] split = currencyPairString.split(counter);
        return new CurrencyPair(split[0], counter);
    }

    public static Instrument adaptSymbol(CoinexMarketInfo coinexMarketInfo) {
        return new CurrencyPair(coinexMarketInfo.getBaseCcy(), coinexMarketInfo.getQuoteCcy());
    }

    public static InstrumentMetaData adaptPair(CoinexMarketInfo coinexMarketInfo) {
        return new InstrumentMetaData.Builder()
                .tradingFee(new BigDecimal(coinexMarketInfo.getTakerFeeRate()))
                .minimumAmount(new BigDecimal(coinexMarketInfo.getMinAmount()))
                .priceScale(coinexMarketInfo.getBaseCcyPrecision())
                .volumeScale(coinexMarketInfo.getQuoteCcyPrecision())
                .feeTiers(null)
                .marketOrderEnabled(true)
                .build();
    }

    public static List<FundingRecord> adaptDepositWithdrawLists(List<CoinexDepositHistory> depositHistory, Object o) {
        List<FundingRecord> result = new ArrayList<>();

        depositHistory.forEach(
                coinexDepositHistory ->
                        result.add(
                                new FundingRecord(
                                        coinexDepositHistory.getToAddress(),
                                        convertToDate(coinexDepositHistory),
                                        Currency.getInstance(coinexDepositHistory.getCcy()),
                                        coinexDepositHistory.getActualAmount(),
                                        coinexDepositHistory.getTxId(),
                                        null,
                                        FundingRecord.Type.DEPOSIT,
                                        getStatus(coinexDepositHistory.getStatus()),
                                        null,
                                        null,
                                        coinexDepositHistory.getRemark())));
        return result;
    }

    private static FundingRecord.Status getStatus(String coinexStatus) {
        switch (coinexStatus) {
            case "finished":
                return FundingRecord.Status.COMPLETE;
            case "cancelled":
                return FundingRecord.Status.CANCELLED;
            case "too_small":
            case "exception":
                return FundingRecord.Status.FAILED;
            default:
                return FundingRecord.Status.PROCESSING;
        }
    }

    private static Date convertToDate(CoinexDepositHistory coinexDepositHistory) {
        return new Date(coinexDepositHistory.getCreatedAt());
    }

    public static DepositAddress adaptDepositAddress(Currency currency,
                                                     CoinexChainInfo chainInfo,
                                                     CoinexDepositAddress depositAddress) {
        return new DepositAddress(
                currency.getCurrencyCode(),
                depositAddress.getAddress(),
                depositAddress.getMemo(),
                chainInfo.getChainName());
    }

    public static CoinexOrderRequestPayload adaptLimitOrder(LimitOrder limitOrder) {
        return new CoinexOrderRequestPayload(
                getMarket((CurrencyPair) limitOrder.getInstrument()),
                "SPOT",
                limitOrder.getType().equals(Order.OrderType.BID) ? "buy" : "sell",
                "limit",
                limitOrder.getOriginalAmount().toPlainString(),
                limitOrder.getLimitPrice().toPlainString());
    }

    public static CoinexCancelOrderRequest adaptCancelOrderParams(CurrencyPair currencyPair, String orderId) {
        return new CoinexCancelOrderRequest(getMarket(currencyPair), "SPOT", Long.valueOf(orderId));
    }

    public static Order adaptOrder(CurrencyPair currencyPair, String orderId, CoinexOrderResponse orderStatus) {

        //filledValue can be null if the order is not recorded by Coinex, fast cancels can cause order to not be recorded
        BigDecimal filledValue = orderStatus.getFilledValue() != null ? orderStatus.getFilledValue() : BigDecimal.ZERO;

        BigDecimal averagePrice =
                filledValue.equals(BigDecimal.ZERO)
                        ? BigDecimal.ZERO
                        : filledValue.divide(orderStatus.getFilledAmount(), RoundingMode.HALF_EVEN);

        BigDecimal fee = BigDecimal.ZERO;
        if (orderStatus.getBaseFee() != null && orderStatus.getBaseFee().doubleValue() != 0.0d) {
            fee = orderStatus.getBaseFee().multiply(averagePrice);
        } else if (orderStatus.getQuoteFee() != null && orderStatus.getQuoteFee().doubleValue() != 0.0d) {
            fee = orderStatus.getQuoteFee();
        }

        return new LimitOrder(
                orderStatus.getType() != null ? orderStatus.getType().equals("buy") ? Order.OrderType.BID : Order.OrderType.ASK : null,
                orderStatus.getAmount(),
                orderStatus.getMarket() != null ? adaptSymbol(orderStatus) : currencyPair,
                orderStatus.getOrderId() != null ? String.valueOf(orderStatus.getOrderId()) : orderId,
                orderStatus.getCreatedAt() != null ? new Date(orderStatus.getCreatedAt()) : null,
                orderStatus.getPrice(),
                averagePrice,
                orderStatus.getFilledAmount() != null ? orderStatus.getFilledAmount() : BigDecimal.ZERO,
                fee,
                orderStatus.getStatus() != null ? adaptOrderStatus(orderStatus.getStatus()) : Order.OrderStatus.CANCELED,
                null);
    }

    /**
     * Adapts the Coinex order status to the XChange order status.
     * <p>
     * open: the order is placed and pending execution;
     * part_filled: order partially executed (still pending);
     * filled: order fully executed (completed);
     * part_canceled: order partially executed and then canceled;
     * canceled: the order is canceled; to maintain server performance, any canceled orders without execution will not be saved.
     */
    private static Order.OrderStatus adaptOrderStatus(String coinexOrderStatus) {
        Order.OrderStatus result;
        switch (coinexOrderStatus) {
            case "part_filled":
                result = Order.OrderStatus.PARTIALLY_FILLED;
                break;
            case "part_canceled":
                result = Order.OrderStatus.PARTIALLY_CANCELED;
                break;
            case "filled":
                result = Order.OrderStatus.FILLED;
                break;
            case "canceled":
                result = Order.OrderStatus.CANCELED;
                break;
            default:
                return Order.OrderStatus.OPEN;
        }
        return result;
    }

    public static Instrument adaptSymbol(CoinexOrderResponse orderStatus) {
        String[] symbolTokenized = orderStatus.getMarket().split(orderStatus.getCcy());
        return new CurrencyPair(orderStatus.getCcy(), symbolTokenized[1]);
    }

    public static CoinexWithdrawRequestPayload adaptWithdrawRequest(DefaultWithdrawFundsParams params) {
        return new CoinexWithdrawRequestPayload(
                getCcy(params.currency),
                params.chain,
                params.address,
                null,
                params.getAddressTag(),
                params.amount.toPlainString(),
                null,
                null);
    }

    public static CandleStickData adaptCandleStickData(CurrencyPair currencyPair, List<CoinexKlineData> coinexKlineData) {
        CandleStickData candleStickData = null;
        if (!coinexKlineData.isEmpty()) {
            List<CandleStick> candleStickList = new ArrayList<>();
            coinexKlineData.forEach(
                    klineData ->
                            candleStickList.add(
                                    new CandleStick.Builder()
                                            .timestamp(new Date(klineData.getCreatedAt()))
                                            .open(klineData.getOpen())
                                            .high(klineData.getHigh())
                                            .low(klineData.getLow())
                                            .close(klineData.getClose())
                                            .volume(klineData.getVolume())
                                            .build()));
            candleStickData = new CandleStickData(currencyPair, candleStickList);
        }

        return candleStickData;
    }

    public static OrderBook adaptMarketDepth(CurrencyPair currencyPair, CoinexMarketDepth marketDepth) {
        List<LimitOrder> bids = new ArrayList<>();
        marketDepth
                .getDepth()
                .getBids()
                .forEach(
                        e ->
                                bids.add(
                                        new LimitOrder(
                                                Order.OrderType.BID,
                                                e.get(1),
                                                currencyPair,
                                                null,
                                                new Date(marketDepth.getDepth().getUpdatedAt()),
                                                e.get(0))));

        List<LimitOrder> asks = new ArrayList<>();
        marketDepth
                .getDepth()
                .getAsks()
                .forEach(
                        e ->
                                asks.add(
                                        new LimitOrder(
                                                Order.OrderType.BID,
                                                e.get(1),
                                                currencyPair,
                                                null,
                                                new Date(marketDepth.getDepth().getUpdatedAt()),
                                                e.get(0))));


        return new OrderBook(new Date(), asks, bids);
    }
}
