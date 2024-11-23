package org.knowm.xchange.coinex.service.account;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.coinex.dto.account.CoinexAssetDetail;
import org.knowm.xchange.coinex.dto.account.CoinexAssets;
import org.knowm.xchange.coinex.dto.account.CoinexBalanceInfo;
import org.knowm.xchange.coinex.service.CoinexBaseService;
import org.knowm.xchange.exceptions.ExchangeException;

import javax.annotation.Nullable;

public class CoinexAccountServiceRaw extends CoinexBaseService {

    protected CoinexAccountServiceRaw(Exchange exchange) {
        super(exchange);
    }

    public Map<String, CoinexBalanceInfo> getCoinexBalances() throws IOException {
        return coinex.getWallet(apiKey, signatureCreator, nonceFactory).getBalances();
    }

    public List<CoinexAssets> getAssetList(@Nullable String currency) {
        List<CoinexAssets> coinexAssetsList;
        try {
            coinexAssetsList =
                    coinex.getAssets(currency != null ? currency.toUpperCase() : null).getData();
        } catch (Exception e) {
            throw new ExchangeException(e);
        }
        return coinexAssetsList;
    }

    public CoinexAssetDetail getAssetDetails(@Nullable String currency) {
        CoinexAssetDetail coinexAssetDetail;
        try {
            coinexAssetDetail =
                    coinex.getAssetDetails(currency != null ? currency.toUpperCase() : null).getData();
        } catch (Exception e) {
            throw new ExchangeException(e);
        }
        return coinexAssetDetail;
    }

}
