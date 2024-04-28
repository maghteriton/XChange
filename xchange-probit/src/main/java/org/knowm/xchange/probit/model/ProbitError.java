package org.knowm.xchange.probit.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProbitError {
  private final String errorCode;
  private final String message;
  private final DetailsDTO details;

  @JsonCreator
  public ProbitError(
      @JsonProperty("errorCode") String errorCode,
      @JsonProperty("message") String message,
      @JsonProperty("details") DetailsDTO details) {
    this.errorCode = errorCode;
    this.message = message;
    this.details = details;
  }

  // Getters
  public String getErrorCode() {
    return errorCode;
  }

  public String getMessage() {
    return message;
  }

  public DetailsDTO getDetails() {
    return details;
  }
}

class DetailsDTO {
  private final String market;

  @JsonCreator
  public DetailsDTO(@JsonProperty("market") String market) {
    this.market = market;
  }

  // Getter
  public String getMarket() {
    return market;
  }
}
