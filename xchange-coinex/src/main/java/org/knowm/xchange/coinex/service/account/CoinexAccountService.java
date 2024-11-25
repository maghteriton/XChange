package org.knowm.xchange.coinex.service.account;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.coinex.CoinexAdapters;
import org.knowm.xchange.coinex.dto.account.*;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.DepositAddress;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.meta.CurrencyChainStatus;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.DefaultWithdrawFundsParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsAll;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;

public class CoinexAccountService extends CoinexAccountServiceRaw implements AccountService {

    public CoinexAccountService(Exchange exchange) {
        super(exchange);
    }

    @Override
    public AccountInfo getAccountInfo() throws IOException {
        return new AccountInfo(CoinexAdapters.adaptWallet(getCoinexBalances()));
    }

    @Override
    public CurrencyChainStatus getCurrencyChainStatus(Currency currency, String chain) throws IOException {
        CoinexAssetDetail assetDetails = getAssetDetails(currency);
        CoinexAssetDetail.Chain coinexChain = assetDetails.getChains().stream()
                .filter(c -> c.getChain().equalsIgnoreCase(chain))
                .findFirst()
                .orElse(null);

        if (coinexChain != null) {

            String contractAddress = coinexChain.getExplorerAssetUrl() != null && coinexChain.getExplorerAssetUrl().contains("/")
                    ? coinexChain.getExplorerAssetUrl().substring(coinexChain.getExplorerAssetUrl().lastIndexOf("/") + 1)
                    : coinexChain.getExplorerAssetUrl();

            return new CurrencyChainStatus(
                    currency,
                    coinexChain.getChain(),
                    contractAddress,
                    coinexChain.isDepositEnabled(),
                    coinexChain.isWithdrawEnabled(),
                    new BigDecimal(coinexChain.getWithdrawalFee()),
                    new BigDecimal(coinexChain.getWithdrawalFee())
            );
        }

        return null; // returns null if not found
    }

    @Override
    public List<FundingRecord> getFundingHistory(TradeHistoryParams params) throws IOException {
        List<FundingRecord> fundingRecords;
        if (params instanceof TradeHistoryParamsAll) {
            TradeHistoryParamsAll tradeHistoryParams = (TradeHistoryParamsAll) params;
            Currency currency = tradeHistoryParams.getCurrencyPair().getBase();
            List<CoinexDepositHistory> depositHistory = getDepositHistory(currency, null, null, null, null);
            fundingRecords = CoinexAdapters.adaptDepositWithdrawLists(depositHistory, null);
            return fundingRecords;
        }

        throw new IllegalStateException("Don't know how to get deposit history: " + params);
    }

    @Override
    public String withdrawFunds(WithdrawFundsParams params) throws IOException {
        if (params instanceof DefaultWithdrawFundsParams) {
            DefaultWithdrawFundsParams defaultWithdrawFundsParams = (DefaultWithdrawFundsParams) params;
            CoinexWithdrawResponse coinexWithdrawResponseCoinexResponse =
                    withdrawFunds(CoinexAdapters.adaptWithdrawRequest(defaultWithdrawFundsParams));

            return String.valueOf(coinexWithdrawResponseCoinexResponse.getWithdrawId());
        }
        throw new IllegalStateException("Don't know how to withdraw: " + params);
    }


    @Override
    public List<DepositAddress> getDepositAddresses(Currency currency) throws IOException {
        List<DepositAddress> depositAddressList = new ArrayList<>();
        List<CoinexAssets> assetList = getAssetList(currency);

        for (CoinexAssets coinexAssets : assetList) {
            coinexAssets.getChainInfo().stream()
                    .map(chainInfo -> {
                        try {
                            return CoinexAdapters.adaptDepositAddress(
                                    currency,
                                    chainInfo,
                                    getSingleDepositAddress(currency, chainInfo.getChainName()));
                        } catch (IOException e) {
                            throw new ExchangeException(e);
                        }
                    })
                    .forEach(depositAddressList::add);
        }

        return depositAddressList;
    }
}
