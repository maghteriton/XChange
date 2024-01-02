package org.knowm.xchange.bingx;

public class BingxException extends RuntimeException {

  private static final long serialVersionUID = 8592824161257095909L;

  private Integer code;

  public BingxException(String message) {
    super(message);
  }

  public BingxException(String message, Throwable cause) {
    super(message, cause);
  }

  public BingxException(Integer code, String message) {
    super(message);
    this.code = code;
  }

  public Integer getCode() {
    return code;
  }

  @Override
  public String toString() {
    return "BingxException{"
        + "code='"
        + getCode()
        + '\''
        + ", message='"
        + getMessage()
        + '\''
        + '}';
  }
}
