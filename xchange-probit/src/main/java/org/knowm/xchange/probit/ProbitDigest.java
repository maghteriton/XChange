package org.knowm.xchange.probit;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Base64;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestInvocation;

public class ProbitDigest implements ParamsDigest {

  private static final String payload = "{\"grant_type\":\"client_credentials\"}";
  private static final String url = "https://accounts.probit.com/token";
  private static final String COLON = ":";
  private static final String BEARER = "Bearer ";
  private final String apiClientId;
  private final String apiClientSecret;
  private AuthorizationDTO authorizationDTO;

  public static ParamsDigest createInstance(String apiKey, String apiClientSecret) {
    return new ProbitDigest(apiKey, apiClientSecret);
  }

  private ProbitDigest(String apiClientId, String apiClientSecret) {
    this.apiClientId = apiClientId;
    this.apiClientSecret = apiClientSecret;
    this.authorizationDTO = fetchToken();
  }

  @Override
  public String digestParams(RestInvocation restInvocation) {
    if (authorizationDTO == null || isTokenExpired(authorizationDTO)) {
      authorizationDTO = fetchToken();
    }
    return authorizationDTO != null ? BEARER + authorizationDTO.getAccessToken() : null;
  }

  private boolean isTokenExpired(AuthorizationDTO authorizationDTO) {
    return authorizationDTO.getExpirationTime() == null
        || Instant.now().isAfter(authorizationDTO.getExpirationTime());
  }

  private AuthorizationDTO fetchToken() {
    // Concatenate and encode credentials
    String concatenatedCredentials = apiClientId + COLON + apiClientSecret;
    String encodedBase64 = Base64.getEncoder().encodeToString(concatenatedCredentials.getBytes());

    // Headers
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("accept", "application/json")
            .header("Authorization", "Basic " + encodedBase64)
            .header("content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(payload))
            .build();

    HttpClient client = HttpClient.newHttpClient();

    // Deserialize JSON response to AuthorizationDTO object using Jackson
    ObjectMapper mapper = new ObjectMapper();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      AuthorizationDTO newAuthorizationDTO =
          mapper.readValue(response.body(), AuthorizationDTO.class);
      // minus 5 alpha value for ensuring token is always up-to-date
      newAuthorizationDTO.setExpirationTime(
          Instant.now().plusSeconds(newAuthorizationDTO.getExpiresIn() - 5));
      return newAuthorizationDTO;
    } catch (Exception e) {
      throw new ProbitException("", e.getMessage(), null);
    }
  }

  public static class AuthorizationDTO {

    private final String accessToken;
    private final String tokenType;
    private final int expiresIn;
    private Instant expirationTime; // Store expiration time

    @JsonCreator
    public AuthorizationDTO(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") int expiresIn) {
      this.accessToken = accessToken;
      this.tokenType = tokenType;
      this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
      return accessToken;
    }

    public String getTokenType() {
      return tokenType;
    }

    public int getExpiresIn() {
      return expiresIn;
    }

    public Instant getExpirationTime() {
      return expirationTime;
    }

    public void setExpirationTime(Instant expirationTime) {
      this.expirationTime = expirationTime;
    }
  }
}
