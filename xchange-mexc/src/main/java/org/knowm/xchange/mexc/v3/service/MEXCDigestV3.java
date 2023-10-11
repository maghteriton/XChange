package org.knowm.xchange.mexc.v3.service;

import static org.knowm.xchange.utils.DigestUtils.bytesToHex;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.crypto.Mac;
import javax.ws.rs.QueryParam;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.BaseParamsDigest;
import si.mazi.rescu.HttpMethod;
import si.mazi.rescu.Params;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestInvocation;

public class MEXCDigestV3 extends BaseParamsDigest {

  public MEXCDigestV3(String secretKeyBase64) {
    super(secretKeyBase64, HMAC_SHA_256);
  }

  public static ParamsDigest createInstance(String secretKey) {
    return new MEXCDigestV3(secretKey);
  }

  @Override
  public String digestParams(RestInvocation restInvocation) {
    String input = getDigestInputParams(restInvocation);
    Mac mac = getMac();
    mac.update(input.getBytes(StandardCharsets.UTF_8));
    return bytesToHex(mac.doFinal());
  }

  private String getDigestInputParams(RestInvocation restInvocation) {
    Map<Class<? extends Annotation>, Params> paramsMap = restInvocation.getParamsMap();

    if (HttpMethod.GET.name().equals(restInvocation.getHttpMethod())
        || HttpMethod.DELETE.name().equals(restInvocation.getHttpMethod())) {
      String queryString =
          Stream.of(restInvocation.getParamsMap().get(QueryParam.class))
              .map(Params::asHttpHeaders)
              .map(Map::entrySet)
              .flatMap(Collection::stream)
              .filter(e -> !"signature".equals(e.getKey()))
              .sorted(Map.Entry.comparingByKey())
              .map(e -> e.getKey() + "=" + encodeValue(e.getValue()))
              .collect(Collectors.joining("&"));

      return queryString;
    }

    /*if (HttpMethod.POST.name().equals(restInvocation.getHttpMethod())) {
      return apiKey + reqTime + restInvocation.getRequestBody();
    }*/
    throw new NotYetImplementedForExchangeException(
        "Only GET, DELETE and POST are supported in digest");
  }

  private String encodeValue(String value) {
    String ret;
    try {
      ret = URLEncoder.encode(value, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException(e.getMessage());
    }
    return ret;
  }
}
