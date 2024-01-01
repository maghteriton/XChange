package org.knowm.xchange.bingx;

import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.service.BaseParamsDigest;
import si.mazi.rescu.Params;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestInvocation;

import javax.crypto.Mac;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import static org.knowm.xchange.utils.DigestUtils.bytesToHex;

public class BingxDigest extends BaseParamsDigest {

  public static final String SIGN_REGEX = "&?signature=[^&]+";

  public static ParamsDigest createInstance(String secretKey) {
    return new BingxDigest(secretKey);
  }

  private BingxDigest(String secretKey) {
    super(secretKey, HMAC_SHA_256);
  }

  @Override
  public String digestParams(RestInvocation restInvocation) {
    Map<Class<? extends Annotation>, Params> paramsMap = restInvocation.getParamsMap();
    Params queryParam = paramsMap.get(QueryParam.class);
    String message = queryParam.asQueryString().replaceAll(SIGN_REGEX, "");
    Mac mac256 = getMac();

    try {
      mac256.update(message.getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      throw new ExchangeException("Digest encoding exception", e);
    }


    return bytesToHex(mac256.doFinal());
  }
}
