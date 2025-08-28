package org.example.config;

import org.example.security.ApiKeyAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for Spring Security settings.
 * Sets up stateless session management, request authorization, and custom filters.
 */
@Configuration
public class SecurityConfig {
    /**
     * Filter for API key authentication.
     */
    @Autowired
    private ApiKeyAuthFilter apiKeyAuthFilter;
    /**
     * Filter for rate limiting requests.
     */
    @Autowired
    private RateLimitFilter rateLimitFilter;

    /**
     * Configures the security filter chain for HTTP requests.
     * @param http the HttpSecurity object
     * @return configured SecurityFilterChain
     * @throws Exception if a security configuration error occurs
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(rateLimitFilter, ApiKeyAuthFilter.class);
        return http.build();
    }
}
