package org.knowm.xchange.coinw;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class CoinwException extends RuntimeException {

  private static final long serialVersionUID = 8592824161257095909L;
  private final String errorCode;
  private final String message;
  private final Map<String, Object> details;

  @JsonCreator
  public CoinwException(
      @JsonProperty("errorCode") String errorCode,
      @JsonProperty("message") String message,
      @JsonProperty("details") Map<String, Object> details) {
    this.errorCode = errorCode;
    this.message = message;
    this.details = details != null ? new HashMap<>(details) : new HashMap<>();
  }

  // Getters
  public String getErrorCode() {
    return errorCode;
  }

  public String getMessage() {
    return message;
  }

  @JsonAnyGetter
  public Map<String, Object> getDetails() {
    return details;
  }
}
