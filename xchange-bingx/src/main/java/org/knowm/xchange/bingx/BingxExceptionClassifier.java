package org.knowm.xchange.bingx;

import java.io.IOException;
import org.knowm.xchange.bingx.dto.BingxResultDTO;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.ExchangeSecurityException;
import org.knowm.xchange.exceptions.ExchangeUnavailableException;

public final class BingxExceptionClassifier {

  BingxExceptionClassifier() {}

  public static <T> T classifyingExceptions(IOExceptionThrowingSupplier<BingxResultDTO<T>> apiCall)
      throws IOException {
    BingxResultDTO<T> response = apiCall.get();
    if (response.isSuccessful()) {
      return response.getData();
    } else {
      throw classify(new BingxException(response.getCode(), response.getMsg()));
    }
  }

  public static RuntimeException classify(BingxException e) {
    if (e.getMessage() == null) {
      return new ExchangeException(null, e);
    }

    if (e.getMessage().equalsIgnoreCase("Service unavailable")) {
      return new ExchangeUnavailableException(e.getMessage(), e);
    }
    if (e.getMessage().contains("check environment variables")) {
      return new ExchangeSecurityException(e.getMessage(), e);
    }

    switch (e.getCode()) {
      case 400:
        return new ExchangeException("Bad Request", e);
      case 401:
        return new ExchangeException("Unauthorized", e);
      case 403:
        return new ExchangeException("Forbidden", e);
      case 404:
        return new ExchangeException("Not Found", e);
      case 429:
        return new ExchangeException("Too Many Requests", e);
      case 418:
        return new ExchangeException("IP Ban (Too Many Requests)", e);
      case 500:
        return new ExchangeException("Internal Server Error", e);
      case 504:
        return new ExchangeException("Unknown Status", e);
      case 100001:
        return new ExchangeException("Signature Verification Failed", e);
      case 100500:
        return new ExchangeException("Internal System Error", e);
      case 80012:
        return new ExchangeException("Service Unavailable", e);
      case 80014:
        return new ExchangeException("Invalid Parameter", e);
      case 80016:
        return new ExchangeException("Order Does Not Exist", e);
      case 80017:
        return new ExchangeException("Position Does Not Exist", e);
      case 80020:
        return new ExchangeException("Risk Forbidden", e);
      case 100004:
        return new ExchangeException("Permission Denied: API Key without Permission", e);
      case 100419:
        return new ExchangeException("IP Does Not Match IP Whitelist", e);
      case 101204:
        return new ExchangeException("Insufficient Margin", e);
      case 80013:
        return new ExchangeException("System Limit: Maximum Entrusted Orders Reached", e);
      case 80018:
        return new ExchangeException("Order is Already Filled", e);
      case 80019:
        return new ExchangeException("Order is Being Processed", e);
      case 100400:
        return new ExchangeException("Arguments Invalid/Miss", e);
      case 100412:
        return new ExchangeException("Null Signature", e);
      case 100413:
        return new ExchangeException("Incorrect API Key", e);
      case 100421:
        return new ExchangeException("Null Timestamp or Timestamp Mismatch", e);
      case 101209:
        return new ExchangeException("Maximum Position Value Exceeded", e);
      case 101211:
        return new ExchangeException("Order Price Should Be Lower/Higher Than Given", e);
      case 101212:
        return new ExchangeException("Order Placement Failed: Check for Pending Orders", e);
      case 101215:
        return new ExchangeException("Maker (Post Only) Order: Immediate Match Cancellation", e);
      case 101414:
        return new ExchangeException("Maximum Leverage Exceeded", e);
      case 101415:
        return new ExchangeException("Trading Pair Suspended: No New Position Allowed", e);
      case 101460:
        return new ExchangeException(
            "Order Price Should Be Higher Than Estimated Liquidation Price", e);
      case 101500:
        return new ExchangeException("RPC Timeout", e);
      case 101514:
        return new ExchangeException("Temporary Suspension: Try Again Later", e);
      case 109201:
        return new ExchangeException("Duplicate Order Submission within 1 Second", e);
      default:
        return new ExchangeException(e.getMessage(), e);
    }
  }

  @FunctionalInterface
  public interface IOExceptionThrowingSupplier<T> {
    T get() throws IOException;
  }
}
