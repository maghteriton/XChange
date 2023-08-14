package org.knowm.xchange.kucoin.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;
import org.knowm.xchange.kucoin.KucoinChain;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrenciesV2Response {

  private String currency;
  private String name;
  private String fullName;
  private Integer precision;
  private Integer confirms;
  private String contractAddress;
  private Boolean isMarginEnabled;
  private Boolean isDebitEnabled;
  private List<KucoinChain> chains;

  public String getCurrency() {
    return currency;
  }

  public String getName() {
    return name;
  }

  public String getFullName() {
    return fullName;
  }

  public Integer getPrecision() {
    return precision;
  }

  public Integer getConfirms() {
    return confirms;
  }

  public String getContractAddress() {
    return contractAddress;
  }

  public Boolean isMarginEnabled() {
    return isMarginEnabled;
  }

  public Boolean isDebitEnabled() {
    return isDebitEnabled;
  }

  public List<KucoinChain> getChains() {
    return chains;
  }
}
