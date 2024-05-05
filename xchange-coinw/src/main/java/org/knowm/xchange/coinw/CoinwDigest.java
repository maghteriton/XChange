package org.knowm.xchange.coinw;

import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestInvocation;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.QueryParam;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class CoinwDigest implements ParamsDigest {

  private final String accessKey;
  private final String secretKey;

  public static ParamsDigest createInstance(String apiKey, String apiClientSecret) {
    return new CoinwDigest(apiKey, apiClientSecret);
  }

  private CoinwDigest(String accessKey, String secretKey) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
  }

  @Override
  public String digestParams(RestInvocation restInvocation) {
    String[] queryParameters = restInvocation.getQueryString().split("&");
    String[] bodyParameters = restInvocation.getRequestBody().replaceAll("[{}\"]", "").replace(":", "=").split(",");
    Map<String, String> httpHeadersFromParams = restInvocation.getHttpHeadersFromParams();
    String[] headers = mapToStringArray(httpHeadersFromParams);

    String[] mergedArray = mergeArrays(queryParameters, bodyParameters);
    mergedArray = mergeArrays(mergedArray, headers);

    Arrays.sort(mergedArray); // Sort parameters alphabetically

    StringBuilder sortedParamsBuilder = new StringBuilder();
    for (String parameter : mergedArray) {
      if(!parameter.isEmpty()) {
        sortedParamsBuilder.append(parameter).append("&");
      }
    }
    // Remove the last "&"
    String sortedParams =
            sortedParamsBuilder.deleteCharAt(sortedParamsBuilder.length() - 1).toString();

    // Append secret key to the sorted parameters
    String toBeSigned = sortedParams + "&secret_key=" + secretKey;

    try {
      // Generate MD5 hash
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] md5Hash = md.digest(toBeSigned.getBytes(StandardCharsets.UTF_8));

/*      // Convert byte array to hexadecimal string
      StringBuilder hexString = new StringBuilder();
      for (byte b : md5Hash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }
      return hexString.toString().toUpperCase();*/

      // Convert byte array to a hexadecimal string
      StringBuilder sb = new StringBuilder();
      for (byte b : md5Hash) {
        sb.append(String.format("%02x", b));
      }
      return sb.toString().toUpperCase();

    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Could not generate MD5 signature", e);
    }
  }

  public static String[] mergeArrays(String[] array1, String[] array2) {
    int length1 = array1.length;
    int length2 = array2.length;

    // Create a new array with combined length
    String[] mergedArray = new String[length1 + length2];

    // Copy elements of array1 to mergedArray
    System.arraycopy(array1, 0, mergedArray, 0, length1);

    // Copy elements of array2 to mergedArray
    System.arraycopy(array2, 0, mergedArray, length1, length2);

    return mergedArray;
  }

  public static String[] mapToStringArray(Map<String, String> map) {
    List<String> keyValuePairs = new ArrayList<>();
    for (Map.Entry<String, String> entry : map.entrySet()) {
      if (!entry.getKey().equals("sign")) {
        String pair = entry.getKey() + "=" + entry.getValue();
        keyValuePairs.add(pair);
      }
    }
    return keyValuePairs.toArray(new String[0]);
  }
}
