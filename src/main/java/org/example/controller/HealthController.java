package org.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for health check endpoint.
 * Provides a simple endpoint to verify service availability.
 */
@RestController
public class HealthController {
    /**
     * Health check endpoint.
     * @return "OK" if the service is running
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
