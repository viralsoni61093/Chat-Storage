package org.example.config;


import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Filter for applying rate limiting to incoming requests using Resilience4j.
 * Allows public paths to bypass rate limiting and restricts requests per client IP.
 */
@Component
public class RateLimitFilter extends OncePerRequestFilter {
    /**
     * Maximum number of requests allowed per minute per client IP.
     * Configurable via 'rate.limit' property (default: 100).
     */
    @Value("${rate.limit:100}")
    private int rateLimit;

    private final Map<String, RateLimiter> limiters = new ConcurrentHashMap<>();
    private RateLimiterRegistry registry;

    private static final String[] PUBLIC_PATHS = {
        "/health",
        "/v3/api-docs/**",
        "/swagger-ui/**"
    };
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * Initializes the RateLimiterRegistry with the configured rate limit.
     */
    @PostConstruct
    public void initRegistry() {
        registry = RateLimiterRegistry.of(
            RateLimiterConfig.custom()
                .limitForPeriod(rateLimit)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .timeoutDuration(Duration.ofMillis(0))
                .build()
        );
    }

    /**
     * Applies rate limiting to incoming requests, bypassing public paths.
     * Responds with HTTP 429 if the rate limit is exceeded.
     * @param request the incoming HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        for (String pattern : PUBLIC_PATHS) {
            if (pathMatcher.match(pattern, path)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        String client = request.getRemoteAddr();
        RateLimiter limiter = limiters.computeIfAbsent(client, k -> registry.rateLimiter(client));
        if (limiter.acquirePermission()) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.getWriter().write("Too Many Requests");
        }
    }
}
