package org.knowm.xchange.coinex.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

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
        private final String minDepositAmount;
        private final String minWithdrawAmount;
        private final boolean depositEnabled;
        private final boolean withdrawEnabled;
        private final int depositDelayMinutes;
        private final int safeConfirmations;
        private final int irreversibleConfirmations;
        private final String deflationRate;
        private final String withdrawalFee;
        private final int withdrawalPrecision;
        private final String memo;
        private final boolean isMemoRequiredForDeposit;
        private final String explorerAssetUrl;

        public Chain(
                @JsonProperty("chain") String chain,
                @JsonProperty("min_deposit_amount") String minDepositAmount,
                @JsonProperty("min_withdraw_amount") String minWithdrawAmount,
                @JsonProperty("deposit_enabled") boolean depositEnabled,
                @JsonProperty("withdraw_enabled") boolean withdrawEnabled,
                @JsonProperty("deposit_delay_minutes") int depositDelayMinutes,
                @JsonProperty("safe_confirmations") int safeConfirmations,
                @JsonProperty("irreversible_confirmations") int irreversibleConfirmations,
                @JsonProperty("deflation_rate") String deflationRate,
                @JsonProperty("withdrawal_fee") String withdrawalFee,
                @JsonProperty("withdrawal_precision") int withdrawalPrecision,
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