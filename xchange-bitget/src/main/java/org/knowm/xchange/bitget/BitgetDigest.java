package org.knowm.xchange.bitget;

import com.google.common.base.Strings;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Mac;
import javax.ws.rs.HeaderParam;

import lombok.Getter;
import org.knowm.xchange.bitget.model.BitgetAPIConstants;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.service.BaseParamsDigest;
import si.mazi.rescu.RestInvocation;

/** Almost identical to Coinbase Pro (even down to the text in the API documentation). */
@Getter
public class BitgetDigest extends BaseParamsDigest {

  private String signature = "";

  private BitgetDigest(byte[] secretKey) {
    super(secretKey, HMAC_SHA_256);
  }

  public static BitgetDigest createInstance(String secretKey) {
    return Strings.isNullOrEmpty(secretKey)
        ? null
        : new BitgetDigest(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public String digestParams(RestInvocation restInvocation) {

    String pathWithQueryString =
        restInvocation.getInvocationUrl().replace(restInvocation.getBaseUrl(), "/");
    String message =
        restInvocation
                .getParamValue(HeaderParam.class, BitgetAPIConstants.ACCESS_TIMESTAMP)
                .toString()
            + restInvocation.getHttpMethod().toUpperCase()
            + pathWithQueryString
            + (restInvocation.getRequestBody() != null ? restInvocation.getRequestBody() : "");

    Mac mac256 = getMac();

    try {
      mac256.update(message.getBytes("UTF-8"));
    } catch (Exception e) {
      throw new ExchangeException("Digest encoding exception", e);
    }

    signature = Base64.getEncoder().encodeToString(mac256.doFinal());
    return signature;
  }
}
