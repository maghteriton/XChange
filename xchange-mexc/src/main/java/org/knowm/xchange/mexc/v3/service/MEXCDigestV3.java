package org.knowm.xchange.mexc.v3.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.crypto.Mac;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.knowm.xchange.service.BaseParamsDigest;
import si.mazi.rescu.HttpMethod;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestInvocation;

import static org.knowm.xchange.utils.DigestUtils.bytesToHex;


public class MEXCDigestV3 extends BaseParamsDigest {

    private static final String SIGNATURE = "signature";

    public MEXCDigestV3(String secretKeyBase64) {
        super(secretKeyBase64, HMAC_SHA_256);
    }

    public static ParamsDigest createInstance(String secretKey) {
        return new MEXCDigestV3(secretKey);
    }

    @Override
    public String digestParams(RestInvocation restInvocation) {
        String input;
        try {
            input = getDigestInputParams(restInvocation);
            Mac mac = getMac();
            mac.update(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(mac.doFinal());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getDigestInputParams(RestInvocation restInvocation) throws JsonProcessingException {

        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> queryParams = restInvocation.getParamsMap().get(QueryParam.class).asHttpHeaders();
        queryParams.remove(SIGNATURE);
        concatParams(queryParams, stringBuilder);

        if (HttpMethod.POST.name().equals(restInvocation.getHttpMethod())) {
            Map<String, String> formParams = restInvocation.getParamsMap().get(FormParam.class).asHttpHeaders();
            formParams.remove(SIGNATURE);
            concatParams(formParams, stringBuilder);
        }

        return stringBuilder.toString();
    }

    private void concatParams(Map<String, String> params, StringBuilder stringBuilder) {

        params.forEach((k, v) -> {
            if (v != null) {
                stringBuilder.append(k).append("=").append(encodeValue(v)).append("&");
            }
        });

        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1); // Remove the last '&'
        }
    }

    private String encodeValue(String value) {
        String ret;
        try {
            ret = URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage());
        }
        return ret;
    }

}
