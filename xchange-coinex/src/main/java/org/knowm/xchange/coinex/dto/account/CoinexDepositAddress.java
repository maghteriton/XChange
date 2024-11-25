package org.knowm.xchange.coinex.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CoinexDepositAddress {

    private final String address;
    private final String memo;

    public CoinexDepositAddress(
            @JsonProperty("address") String address,
            @JsonProperty("memo") String memo) {
        this.address = address;
        this.memo = memo;
    }
}
