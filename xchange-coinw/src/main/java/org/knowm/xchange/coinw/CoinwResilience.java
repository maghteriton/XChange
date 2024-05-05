package org.knowm.xchange.coinw;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.client.ResilienceUtils;

import java.time.Duration;

import static javax.ws.rs.core.Response.Status.TOO_MANY_REQUESTS;

public class CoinwResilience {

  public static final String PUBLIC_RATE_LIMITS = "PUBLIC_RATE_LIMITS";

  public static final String PRIVATE_RATE_LIMITS = "PRIVATE_RATE_LIMITS";

  public static ResilienceRegistries createRegistries() {
    final ResilienceRegistries registries = new ResilienceRegistries();

    registries
        .rateLimiters()
        .rateLimiter(
            PUBLIC_RATE_LIMITS,
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
            PRIVATE_RATE_LIMITS,
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
