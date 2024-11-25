package org.knowm.xchange.coinex.service.account;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.coinex.CoinexAdapters;
import org.knowm.xchange.coinex.dto.account.*;
import org.knowm.xchange.coinex.service.CoinexBaseService;
import org.knowm.xchange.currency.Currency;

import javax.annotation.Nullable;

public class CoinexAccountServiceRaw extends CoinexBaseService {

    protected CoinexAccountServiceRaw(Exchange exchange) {
        super(exchange);
    }

    public Map<String, CoinexBalanceInfo> getCoinexBalances() throws IOException {
        return coinex.getWallet(apiKey, signatureCreator, nonceFactory).getBalances();
    }

    public List<CoinexAssets> getAssetList(@Nullable Currency currency) throws IOException {
        return checkResult(coinex.getAssets(CoinexAdapters.getCcy(currency)));
    }

    public CoinexAssetDetail getAssetDetails(@Nullable Currency currency) throws IOException {
        return checkResult(coinex.getAssetDetails(CoinexAdapters.getCcy(currency)));
    }

    public List<CoinexDepositHistory> getDepositHistory(
            @Nullable Currency currency,
            @Nullable String txId,
            @Nullable String status,
            @Nullable String page,
            @Nullable String limit
    ) throws IOException {
        return checkResult(coinex.getDepositHistory(apiKey,
                signatureCreator,
                nonceFactory,
                CoinexAdapters.getCcy(currency),
                txId,
                status,
                page,
                limit));
    }

    public CoinexDepositAddress getSingleDepositAddress(Currency currency, String chain) throws IOException {
        return checkResult(coinex
                .getDepositAddress(apiKey,
                        signatureCreator,
                        nonceFactory,
                        CoinexAdapters.getCcy(currency),
                        chain));
    }

    public CoinexWithdrawResponse withdrawFunds(CoinexWithdrawRequestPayload coinexWithdrawRequestPayload) throws IOException {
        return checkResult(coinex.withdrawRequest(apiKey,
                signatureCreator,
                nonceFactory, coinexWithdrawRequestPayload));
    }
}
