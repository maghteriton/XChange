package org.knowm.xchange.coinex.service;

import org.knowm.xchange.service.BaseParamsDigest;
import si.mazi.rescu.RestInvocation;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import static org.knowm.xchange.coinex.dto.APIConstants.X_COINEX_TIMESTAMP;
import static org.knowm.xchange.utils.DigestUtils.bytesToHex;

public class CoinexDigest extends BaseParamsDigest {

    private final String secretKey;

    private CoinexDigest(String secretKey, String apiKey) {
        super(secretKey, HMAC_SHA_256);
        this.secretKey = secretKey;
    }

    public static CoinexDigest createInstance(String apiKey, String secretKey) {
        return new CoinexDigest(secretKey, apiKey);
    }

    @Override
    public String digestParams(RestInvocation restInvocation) {
        String method = restInvocation.getHttpMethod();
        String requestPath = restInvocation.getPath();
        String queryString = restInvocation.getQueryString();
        String body = restInvocation.getRequestBody();
        String timestamp = restInvocation.getHttpHeadersFromParams().get(X_COINEX_TIMESTAMP);

        String preparedStr = method
                + requestPath
                + (queryString != null && !queryString.isEmpty() ? "?" + queryString : "")
                + (body != null ? body : "") + timestamp;

        try {
            Mac mac = Mac.getInstance(HMAC_SHA_256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA_256);
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(preparedStr.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash).toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException("Unable to generate HMAC-SHA256 signature", e);
        }
    }
}
