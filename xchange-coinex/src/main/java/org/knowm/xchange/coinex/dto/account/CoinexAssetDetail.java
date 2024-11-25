package org.knowm.xchange.coinex.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@ToString
public class CoinexAssetDetail {

    private final Asset asset;
    private final List<Chain> chains;

    public CoinexAssetDetail(
            @JsonProperty("asset") Asset asset,
            @JsonProperty("chains") List<Chain> chains) {
        this.asset = asset;
        this.chains = chains;
    }

    @Getter
    @ToString
    public static class Asset {
        private final String ccy;
        private final boolean depositEnabled;
        private final boolean withdrawEnabled;
        private final boolean interTransferEnabled;
        private final boolean isSt;

        public Asset(
                @JsonProperty("ccy") String ccy,
                @JsonProperty("deposit_enabled") boolean depositEnabled,
                @JsonProperty("withdraw_enabled") boolean withdrawEnabled,
                @JsonProperty("inter_transfer_enabled") boolean interTransferEnabled,
                @JsonProperty("is_st") boolean isSt) {
            this.ccy = ccy;
            this.depositEnabled = depositEnabled;
            this.withdrawEnabled = withdrawEnabled;
            this.interTransferEnabled = interTransferEnabled;
            this.isSt = isSt;
        }
    }

    @Getter
    @ToString
    public static class Chain {
        private final String chain;
        private final BigDecimal minDepositAmount;
        private final BigDecimal minWithdrawAmount;
        private final boolean depositEnabled;
        private final boolean withdrawEnabled;
        private final Long depositDelayMinutes;
        private final Long safeConfirmations;
        private final Long irreversibleConfirmations;
        private final BigDecimal deflationRate;
        private final BigDecimal withdrawalFee;
        private final Integer withdrawalPrecision;
        private final String memo;
        private final boolean isMemoRequiredForDeposit;
        private final String explorerAssetUrl;

        public Chain(
                @JsonProperty("chain") String chain,
                @JsonProperty("min_deposit_amount") BigDecimal minDepositAmount,
                @JsonProperty("min_withdraw_amount") BigDecimal minWithdrawAmount,
                @JsonProperty("deposit_enabled") boolean depositEnabled,
                @JsonProperty("withdraw_enabled") boolean withdrawEnabled,
                @JsonProperty("deposit_delay_minutes") Long depositDelayMinutes,
                @JsonProperty("safe_confirmations") Long safeConfirmations,
                @JsonProperty("irreversible_confirmations") Long irreversibleConfirmations,
                @JsonProperty("deflation_rate") BigDecimal deflationRate,
                @JsonProperty("withdrawal_fee") BigDecimal withdrawalFee,
                @JsonProperty("withdrawal_precision") Integer withdrawalPrecision,
                @JsonProperty("memo") String memo,
                @JsonProperty("is_memo_required_for_deposit") boolean isMemoRequiredForDeposit,
                @JsonProperty("explorer_asset_url") String explorerAssetUrl) {
            this.chain = chain;
            this.minDepositAmount = minDepositAmount;
            this.minWithdrawAmount = minWithdrawAmount;
            this.depositEnabled = depositEnabled;
            this.withdrawEnabled = withdrawEnabled;
            this.depositDelayMinutes = depositDelayMinutes;
            this.safeConfirmations = safeConfirmations;
            this.irreversibleConfirmations = irreversibleConfirmations;
            this.deflationRate = deflationRate;
            this.withdrawalFee = withdrawalFee;
            this.withdrawalPrecision = withdrawalPrecision;
            this.memo = memo;
            this.isMemoRequiredForDeposit = isMemoRequiredForDeposit;
            this.explorerAssetUrl = explorerAssetUrl;
        }
    }
}