package org.knowm.xchange.huobi.dto.marketdata.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.huobi.dto.HuobiResult;
import org.knowm.xchange.huobi.dto.marketdata.HuobiChain;

public class HuobiChainsResult  extends HuobiResult<HuobiChain[]> {

    public HuobiChainsResult(
            @JsonProperty("status") String status,
            @JsonProperty("data") HuobiChain[] result,
            @JsonProperty("err-code") String errCode,
            @JsonProperty("err-msg") String errMsg) {
        super(status, errCode, errMsg, result);
    }
}
