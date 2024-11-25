package org.knowm.xchange.coinex.dto.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoinexWithdrawRequestPayload {

    @JsonProperty("ccy")
    private final String ccy;
    @JsonProperty("chain")
    private final String chain;
    @JsonProperty("to_address")
    private final String toAddress;
    @JsonProperty("withdraw_method")
    private final String withdrawMethod;
    @JsonProperty("memo")
    private final String memo;
    @JsonProperty("amount")
    private final String amount;
    @JsonProperty("extra")
    private final Extra extra;
    @JsonProperty("remark")
    private final String remark;

    public CoinexWithdrawRequestPayload(
            @JsonProperty("ccy") String ccy,
            @JsonProperty("chain") String chain,
            @JsonProperty("to_address") String toAddress,
            @JsonProperty("withdraw_method") String withdrawMethod,
            @JsonProperty("memo") String memo,
            @JsonProperty("amount") String amount,
            @JsonProperty("extra") Extra extra,
            @JsonProperty("remark") String remark) {
        this.ccy = ccy;
        this.chain = chain;
        this.toAddress = toAddress;
        this.withdrawMethod = withdrawMethod;
        this.memo = memo;
        this.amount = amount;
        this.extra = extra;
        this.remark = remark;
    }

    @Getter
    @ToString
    public static class Extra {
        @JsonProperty("chain_id")
        private final String chainId;

        public Extra(@JsonProperty("chain_id") String chainId) {
            this.chainId = chainId;
        }
    }
}
