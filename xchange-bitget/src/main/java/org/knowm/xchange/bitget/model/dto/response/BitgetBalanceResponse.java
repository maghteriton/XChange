package org.knowm.xchange.bitget.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BitgetBalanceResponse {
    private String coin;
    private String available;
    private String frozen;
    private String locked;
    private String limitAvailable;
    private String uTime;
}
