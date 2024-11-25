package org.knowm.xchange.coinex;

import com.fasterxml.jackson.annotation.JsonProperty;
import si.mazi.rescu.HttpStatusExceptionSupport;

public class CoinexException extends HttpStatusExceptionSupport {

  private final int code;

  public CoinexException(
      @JsonProperty("code") int code, @JsonProperty("message") String message) {
    super(message);
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
