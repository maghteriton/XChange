package org.knowm.xchange.probit;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.client.ResilienceUtils;

import java.time.Duration;

import static javax.ws.rs.core.Response.Status.TOO_MANY_REQUESTS;

public class ProbitResilience {

  public static final String GROUP_1_ENDPOINT_RATE_LIMITER = "group1EndpointLimit";

  public static final String GROUP_2_ENDPOINT_RATE_LIMITER = "group2EndpointLimit";
  public static final String GROUP_3_ENDPOINT_RATE_LIMITER = "group3EndpointLimit";

  public static ResilienceRegistries createRegistries() {
    final ResilienceRegistries registries = new ResilienceRegistries();

    registries
        .rateLimiters()
        .rateLimiter(
                GROUP_1_ENDPOINT_RATE_LIMITER,
            RateLimiterConfig.from(registries.rateLimiters().getDefaultConfig())
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(10)
                .timeoutDuration(Duration.ofMillis(500))
                .drainPermissionsOnResult(
                    e -> ResilienceUtils.matchesHttpCode(e, TOO_MANY_REQUESTS))
                .build());

    registries
        .rateLimiters()
        .rateLimiter(
            GROUP_2_ENDPOINT_RATE_LIMITER,
            RateLimiterConfig.from(registries.rateLimiters().getDefaultConfig())
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(20)
                .timeoutDuration(Duration.ofMillis(500))
                .drainPermissionsOnResult(
                    e -> ResilienceUtils.matchesHttpCode(e, TOO_MANY_REQUESTS))
                .build());

    registries
            .rateLimiters()
            .rateLimiter(
                    GROUP_3_ENDPOINT_RATE_LIMITER,
                    RateLimiterConfig.from(registries.rateLimiters().getDefaultConfig())
                            .limitRefreshPeriod(Duration.ofSeconds(1))
                            .limitForPeriod(20)
                            .timeoutDuration(Duration.ofMillis(500))
                            .drainPermissionsOnResult(
                                    e -> ResilienceUtils.matchesHttpCode(e, TOO_MANY_REQUESTS))
                            .build());

    return registries;
  }
}
