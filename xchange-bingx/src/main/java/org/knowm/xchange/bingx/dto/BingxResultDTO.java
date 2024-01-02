package org.knowm.xchange.bingx.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class BingxResultDTO<T> {

  private static final int SUCCESS_CODE_200 = 200;
  private static final int SUCCESS_CODE_0 = 0;
  private final Integer code;
  private final Date timestamp;
  private final String msg;
  private final String debugMsg;
  private final T data;

  @JsonCreator
  public BingxResultDTO(
      @JsonProperty("code") Integer code,
      @JsonProperty("timestamp") Date timestamp,
      @JsonProperty("msg") String msg,
      @JsonProperty("debugMsg") String debugMsg,
      @JsonProperty("data") T data) {
    this.code = code;
    this.timestamp = timestamp;
    this.msg = msg;
    this.debugMsg = debugMsg;
    this.data = data;
  }

  public boolean isSuccessful() {
    return SUCCESS_CODE_200 == this.code || SUCCESS_CODE_0 == this.code;
  }

  public Integer getCode() {
    return code;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public String getMsg() {
    return msg;
  }

  public String getDebugMsg() {
    return debugMsg;
  }

  public T getData() {
    return data;
  }
}
