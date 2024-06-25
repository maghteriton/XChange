package org.knowm.xchange.bitget.model.dto.response;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.knowm.xchange.bitget.FeeDetailDeserializer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BitgetOrderHistoryResponse {

  private String userId;
  private String symbol;
  private String orderId;
  private String clientOid;
  private BigDecimal price;
  private BigDecimal size;
  private String orderType;
  private String side;
  private String status;
  private BigDecimal priceAvg;
  private BigDecimal baseVolume;
  private BigDecimal quoteVolume;
  private String enterPointSource;

  @JsonDeserialize(using = FeeDetailDeserializer.class)
  private BitgetFeeDetail feeDetail;

  private String orderSource;
  private String cTime;
  private String uTime;
  private String tpslType;
  private String triggerPrice;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class BitgetFeeDetail {
    @JsonProperty("newFees")
    private FeeDetailItem newFees;

    // Map to handle dynamic fee items like XLM, USDT, etc.
    private Map<String, FeeDetailItem> feeDetails = new HashMap<>();

    @JsonAnySetter
    public void addFeeDetail(String key, FeeDetailItem value) {
      feeDetails.put(key, value);
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FeeDetailItem {
      private boolean deduction;
      private String feeCoinCode;
      private BigDecimal totalDeductionFee;
      private BigDecimal totalFee;

      // These fields are only present in newFees, so they can be null for other feeDetails
      private BigDecimal c;
      private BigDecimal d;
      private BigDecimal r;
      private BigDecimal t;
    }
  }
}
