package org.knowm.xchange.coinex.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class CoinexDepositHistory {

    private final Long depositId;
    private final Long createdAt;
    private final String txId;
    private final String ccy;
    private final String chain;
    private final String depositMethod;
    private final BigDecimal amount;
    private final BigDecimal actualAmount;
    private final String toAddress;
    private final Long confirmations;
    private final String status;
    private final String txExplorerUrl;
    private final String toAddrExplorerUrl;
    private final String remark;

    public CoinexDepositHistory(
            @JsonProperty("deposit_id") Long depositId,
            @JsonProperty("created_at") Long createdAt,
            @JsonProperty("tx_id") String txId,
            @JsonProperty("ccy") String ccy,
            @JsonProperty("chain") String chain,
            @JsonProperty("deposit_method") String depositMethod,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("actual_amount") BigDecimal actualAmount,
            @JsonProperty("to_address") String toAddress,
            @JsonProperty("confirmations") Long confirmations,
            @JsonProperty("status") String status,
            @JsonProperty("tx_explorer_url") String txExplorerUrl,
            @JsonProperty("to_addr_explorer_url") String toAddrExplorerUrl,
            @JsonProperty("remark") String remark) {
        this.depositId = depositId;
        this.createdAt = createdAt;
        this.txId = txId;
        this.ccy = ccy;
        this.chain = chain;
        this.depositMethod = depositMethod;
        this.amount = amount;
        this.actualAmount = actualAmount;
        this.toAddress = toAddress;
        this.confirmations = confirmations;
        this.status = status;
        this.txExplorerUrl = txExplorerUrl;
        this.toAddrExplorerUrl = toAddrExplorerUrl;
        this.remark = remark;
    }
}
