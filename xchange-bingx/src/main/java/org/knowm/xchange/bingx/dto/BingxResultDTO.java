package org.knowm.xchange.bingx.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class BingxResultDTO<T> {

  private final int code;
  private final Date timestamp;
  private final String msg;
  private final String debugMsg;
  private final T data;

  @JsonCreator
  public BingxResultDTO(
      @JsonProperty("code") int code,
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

  public int getCode() {
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
