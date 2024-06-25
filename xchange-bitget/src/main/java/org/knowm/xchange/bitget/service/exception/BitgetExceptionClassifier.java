package org.knowm.xchange.bitget.service.exception;

import org.knowm.xchange.bitget.model.BitgetResponse;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.ExchangeSecurityException;
import org.knowm.xchange.exceptions.ExchangeUnavailableException;
import org.knowm.xchange.exceptions.NonceException;

import java.io.IOException;

public class BitgetExceptionClassifier {

  BitgetExceptionClassifier() {}

  public static <T> T classifyingExceptions(IOExceptionThrowingSupplier<BitgetResponse<T>> apiCall)
      throws IOException {
    BitgetResponse<T> response = apiCall.get();
    if (response.isSuccessful()) {
      return response.getData();
    } else {
      throw classify(new BitgetApiException(response.getCode(), response.getMsg()));
    }
  }

  public static RuntimeException classify(BitgetApiException e) {
    if (e.getMessage() == null) {
      return new ExchangeException(null, e);
    }

    switch (e.getCode()) {
      case "12018":
        return new ExchangeUnavailableException(e.getMessage(), e);
      case "40002":
      case "40003":
      case "40009":
      case "49002":
      case "49051":
      case "49052":
        return new ExchangeSecurityException(e.getMessage(), e);
      case "40004":
      case "40005":
      case "40008":
        return new NonceException(e.getMessage(), e);
      default:
        return new ExchangeException(e.getMessage(), e);
    }
  }

  @FunctionalInterface
  public interface IOExceptionThrowingSupplier<T> {
    T get() throws IOException;
  }
}
