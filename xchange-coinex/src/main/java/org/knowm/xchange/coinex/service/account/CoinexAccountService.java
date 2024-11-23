package org.knowm.xchange.coinex.service.account;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.coinex.CoinexAdapters;
import org.knowm.xchange.coinex.dto.account.CoinexAssetDetail;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.meta.CurrencyChainStatus;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
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
        CoinexAssetDetail assetDetails = getAssetDetails(currency.getCurrencyCode());
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
}
