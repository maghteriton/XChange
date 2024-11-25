package org.knowm.xchange.coinex.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class CoinexWithdrawResponse {

    private final Long withdrawId;
    private final Long createdAt;
    private final String ccy;
    private final String chain;
    private final String withdrawMethod;
    private final String memo;
    private final BigDecimal amount;
    private final BigDecimal actualAmount;
    private final String txFee;
    private final String txId;
    private final String toAddress;
    private final Long confirmations;
    private final String explorerAddressUrl;
    private final String explorerTxUrl;
    private final String status;
    private final String remark;

    public CoinexWithdrawResponse(
            @JsonProperty("withdraw_id") Long withdrawId,
            @JsonProperty("created_at") Long createdAt,
            @JsonProperty("ccy") String ccy,
            @JsonProperty("chain") String chain,
            @JsonProperty("withdraw_method") String withdrawMethod,
            @JsonProperty("memo") String memo,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("actual_amount") BigDecimal actualAmount,
            @JsonProperty("tx_fee") String txFee,
            @JsonProperty("tx_id") String txId,
            @JsonProperty("to_address") String toAddress,
            @JsonProperty("confirmations") Long confirmations,
            @JsonProperty("explorer_address_url") String explorerAddressUrl,
            @JsonProperty("explorer_tx_url") String explorerTxUrl,
            @JsonProperty("status") String status,
            @JsonProperty("remark") String remark) {
        this.withdrawId = withdrawId;
        this.createdAt = createdAt;
        this.ccy = ccy;
        this.chain = chain;
        this.withdrawMethod = withdrawMethod;
        this.memo = memo;
        this.amount = amount;
        this.actualAmount = actualAmount;
        this.txFee = txFee;
        this.txId = txId;
        this.toAddress = toAddress;
        this.confirmations = confirmations;
        this.explorerAddressUrl = explorerAddressUrl;
        this.explorerTxUrl = explorerTxUrl;
        this.status = status;
        this.remark = remark;
    }
}
