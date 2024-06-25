package org.knowm.xchange.bitget.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BitgetCoinsResponse {

  private String coinId;
  private String coin;
  private String transfer;
  private List<Chain> chains;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Chain {
    private String chain;
    private Boolean needTag;
    private Boolean withdrawable;
    private Boolean rechargeable;
    private BigDecimal withdrawFee;
    private BigDecimal extraWithdrawFee;
    private Integer depositConfirm;
    private Integer withdrawConfirm;
    private BigDecimal minDepositAmount;
    private BigDecimal minWithdrawAmount;
    private String browserUrl;
    private String contractAddress;
    private String withdrawStep;
    private String withdrawMinScale;
  }
}
