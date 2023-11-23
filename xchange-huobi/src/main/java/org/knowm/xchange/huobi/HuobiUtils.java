package org.knowm.xchange.huobi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.huobi.dto.account.HuobiTransactFeeRate;
import org.knowm.xchange.huobi.dto.marketdata.HuobiAsset;
import org.knowm.xchange.huobi.dto.marketdata.HuobiAssetPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.mazi.rescu.SynchronizedValueFactory;

public class HuobiUtils {
  private static Logger logger = LoggerFactory.getLogger(HuobiUtils.class);
  private static Map<String, CurrencyPair> assetPairMap = new HashMap<>();

  private static Map<String, HuobiTransactFeeRate> huobiTransactFeeRateMap = new HashMap<>();
  private static Map<CurrencyPair, String> assetPairMapReverse = new HashMap<>();
  private static Map<String, Currency> assetMap = new HashMap<>();
  private static Map<Currency, String> assetMapReverse = new HashMap<>();

  private HuobiUtils() {}

  public static String createHuobiCurrencyPair(CurrencyPair currencyPair) {
    String pair = assetPairMapReverse.get(currencyPair);
    if ((pair == null) || (pair.length() == 0)) {
      throw new ExchangeException(
          String.format("Huobi doesn't support currency pair %s", currencyPair.toString()));
    }
    return pair;
  }

  public static String getHuobiCurrencyPairString(CurrencyPair currencyPair) {
    return String.format(
        "%s%s",
        currencyPair.base.getCurrencyCode().toLowerCase(),
        currencyPair.counter.getCurrencyCode().toLowerCase());
  }

  public static String createUTCDate(SynchronizedValueFactory<Long> nonce) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    return dateFormat.format(new Date(nonce.createValue()));
  }

  public static String createUTCDate(Date date) {
    if (date == null) {
      return null;
    }
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return dateFormat.format(date);
  }

  public static void setHuobiAssets(HuobiAsset[] huobiAssets) {
    for (HuobiAsset entry : huobiAssets) {
      assetMap.put(entry.getAsset(), Currency.getInstance(entry.getAsset()));
      assetMapReverse.put(Currency.getInstance(entry.getAsset()), entry.getAsset());
    }
  }

  public static void setHuobiAssetPairs(HuobiAssetPair[] huobiAssetPairs) {
    for (HuobiAssetPair entry : huobiAssetPairs) {
      CurrencyPair pair =
          new CurrencyPair(
              translateHuobiCurrencyCode(entry.getBaseCurrency()),
              translateHuobiCurrencyCode(entry.getQuoteCurrency()));
      if (pair.base != null && pair.counter != null) {
        assetPairMap.put(entry.getKey(), pair);
        assetPairMapReverse.put(pair, entry.getKey());
      }
    }
  }

  public static void setFeeData(List<HuobiTransactFeeRate> transactFeeRate) {
    for (HuobiTransactFeeRate huobiTransactFeeRate : transactFeeRate) {
      huobiTransactFeeRateMap.put(huobiTransactFeeRate.getSymbol(), huobiTransactFeeRate);
    }
  }

  public static Currency translateHuobiCurrencyCode(String currencyIn) {
    Currency currencyOut = assetMap.get(currencyIn);
    if (currencyOut == null) {
      logger.error("Huobi does not support the currency code " + currencyIn);
      return null;
    }
    return currencyOut.getCommonlyUsedCurrency();
  }

  public static CurrencyPair translateHuobiCurrencyPair(String currencyPairIn) {
    CurrencyPair pair = assetPairMap.get(currencyPairIn);
    if (pair == null) {
      if (currencyPairIn.length() == 6) {
        Currency base = Currency.getInstance(currencyPairIn.substring(0, 3));
        if (base.getCommonlyUsedCurrency() != null) {
          base = base.getCommonlyUsedCurrency();
        }
        Currency counter = Currency.getInstance(currencyPairIn.substring(3, 6));
        if (counter.getCommonlyUsedCurrency() != null) {
          counter = counter.getCommonlyUsedCurrency();
        }
        pair = new CurrencyPair(base, counter);
      } else if (currencyPairIn.length() == 7) {
        Currency base = Currency.getInstance(currencyPairIn.substring(0, 4));
        if (base.getCommonlyUsedCurrency() != null) {
          base = base.getCommonlyUsedCurrency();
        }
        Currency counter = Currency.getInstance(currencyPairIn.substring(4, 7));
        if (counter.getCommonlyUsedCurrency() != null) {
          counter = counter.getCommonlyUsedCurrency();
        }
        pair = new CurrencyPair(base, counter);
      }
    }
    return pair;
  }

  public static HuobiTransactFeeRate getFeeRate(String symbol) {
    return huobiTransactFeeRateMap.get(symbol);
  }
}
