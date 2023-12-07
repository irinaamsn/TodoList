package fintech.todolist.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4jConfig {
    @Bean
    public RateLimiter userResurcesRateLimiter(RateLimiterRegistry rateLimiterRegistry) {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(15)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .timeoutDuration(Duration.ofMillis(100))
                .build();
        return rateLimiterRegistry.rateLimiter("userRateLimiter", config);
    }

    @Bean
    public RateLimiterRegistry rateLimiterRegistry(RateLimiterConfig rateLimiterConfig) {
        return RateLimiterRegistry.of(rateLimiterConfig);
    }

}
