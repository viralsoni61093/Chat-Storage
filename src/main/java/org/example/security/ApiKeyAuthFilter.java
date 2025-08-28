package org.example.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter for API key authentication.
 * Validates the 'X-API-KEY' header for protected endpoints and allows public paths.
 */
@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {
    /**
     * The API key used for authentication, configured via 'api.key' property.
     */
    @Value("${api.key}")
    private String apiKey;

    private static final String[] PUBLIC_PATHS = {
            "/health",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final Logger logger = LoggerFactory.getLogger(ApiKeyAuthFilter.class);

    /**
     * Performs API key validation for incoming requests, bypassing public paths.
     * Responds with HTTP 401 if the API key is invalid or missing.
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
        String requestApiKey = request.getHeader("X-API-KEY");
        logger.info("ApiKeyAuthFilter: path={}, apiKeyHeader={}", path, requestApiKey);
        for (String pattern : PUBLIC_PATHS) {
            if (pathMatcher.match(pattern, path)) {
                logger.info("ApiKeyAuthFilter: skipping API key check for public path: {}", path);
                filterChain.doFilter(request, response);
                return;
            }
        }
        if (apiKey != null && !apiKey.isEmpty() && !apiKey.equals(requestApiKey)) {
            logger.warn("ApiKeyAuthFilter: invalid API key for path: {}", path);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid API Key");
            return;
        }
        logger.info("ApiKeyAuthFilter: valid API key for path: {}", path);
        filterChain.doFilter(request, response);
    }
}