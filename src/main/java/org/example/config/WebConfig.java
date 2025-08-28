package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for Spring MVC settings.
 * Registers interceptors for handling HTTP requests.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * Interceptor for logging HTTP requests.
     */
    @Autowired
    private LoggingInterceptor loggingInterceptor;

    /**
     * Adds the LoggingInterceptor to the interceptor registry.
     * @param registry the InterceptorRegistry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
    }
}
