package org.knowm.xchange.mexc.v3.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class WithdrawResponse {

    private final String id;

    public WithdrawResponse(@JsonProperty("id") String id) {
        this.id = id;
    }

}
