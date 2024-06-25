/** Copyright 2019 Mek Global Limited. */
package org.knowm.xchange.bitget.service.exception;

import lombok.Getter;

/** Based on code by zicong.lu on 2018/12/14. */
@Getter
public class BitgetApiException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  private String code;
  private String msg;

  public BitgetApiException(String code, String msg, Throwable throwable) {
    super(msg, throwable);
    this.code = code;
    this.msg = msg;
  }

  public BitgetApiException(String code, String msg) {
    super();
    this.code = code;
    this.msg = msg;
  }

  public BitgetApiException(String msg) {
    super(msg);
    this.msg = msg;
  }
}
