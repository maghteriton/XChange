package org.knowm.xchange.coinex.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class CoinexAssets {

    private final String shortName;
    private final String fullName;
    private final String websiteUrl;
    private final String whitePaperUrl;
    private final List<CoinexChainInfo> chainInfo;

    public CoinexAssets(
            @JsonProperty("short_name") String shortName,
            @JsonProperty("full_name") String fullName,
            @JsonProperty("website_url") String websiteUrl,
            @JsonProperty("white_paper_url") String whitePaperUrl,
            @JsonProperty("chain_info") List<CoinexChainInfo> chainInfo) {
        this.shortName = shortName;
        this.fullName = fullName;
        this.websiteUrl = websiteUrl;
        this.whitePaperUrl = whitePaperUrl;
        this.chainInfo = chainInfo;
    }
}
