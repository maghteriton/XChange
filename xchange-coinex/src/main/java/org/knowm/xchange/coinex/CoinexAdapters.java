package org.knowm.xchange.coinex;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knowm.xchange.coinex.dto.account.CoinexAssets;
import org.knowm.xchange.coinex.dto.account.CoinexBalanceInfo;
import org.knowm.xchange.coinex.dto.market.CoinexMarketInfo;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.meta.CurrencyMetaData;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.meta.InstrumentMetaData;
import org.knowm.xchange.instrument.Instrument;

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
            List<CoinexAssets> coinexAssetList) {
        Map<Instrument, InstrumentMetaData> pairs = new HashMap<>();
        for (CoinexMarketInfo marketInfo : coinexMarketInfoList) {
            if (marketInfo.getQuoteCcy().equalsIgnoreCase(Currency.USDT.getCurrencyCode())) {
                CurrencyPair currencyPair = (CurrencyPair) adaptSymbol(marketInfo);
                pairs.put(currencyPair, adaptPair(marketInfo));
            }
        }

        Map<Currency, CurrencyMetaData> currencies = new HashMap<>();
        for (CoinexAssets coinexAssets : coinexAssetList) {
            String currency = coinexAssets.getShortName();
            CurrencyMetaData currencyMetaData =
                    new CurrencyMetaData(
                            null,
                            null,
                            null,
                            null);
            currencies.put(new Currency(currency), currencyMetaData);
        }

        return new ExchangeMetaData(pairs, currencies, null, null, null);
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
}
