package org.knowm.xchange.coinex.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CoinexChainInfo {

    private final String chainName;
    private final String identity;
    private final String explorerUrl;

    public CoinexChainInfo(
            @JsonProperty("chain_name") String chainName,
            @JsonProperty("identity") String identity,
            @JsonProperty("explorer_url") String explorerUrl) {
        this.chainName = chainName;
        this.identity = identity;
        this.explorerUrl = explorerUrl;
    }
}
