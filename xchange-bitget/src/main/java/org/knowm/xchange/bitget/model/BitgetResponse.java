package org.knowm.xchange.bitget.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;

@Getter
public class BitgetResponse<T> {

  private final String code;
  private final String msg;
  private final Date requestTime;

  private final T data;

  @JsonCreator
  public BitgetResponse(
      @JsonProperty("code") String code,
      @JsonProperty("msg") String msg,
      @JsonProperty("requestTime") Date requestTime,
      @JsonProperty("data") T data) {
    this.code = code;
    this.msg = msg;
    this.requestTime = requestTime;
    this.data = data;
  }

  public boolean isSuccessful() {
    return code.equals("00000") && msg.equals("success");
  }
}
