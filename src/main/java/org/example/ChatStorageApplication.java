package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the RAG Chat Storage Microservice.
 * This class bootstraps the Spring Boot application.
 */
@SpringBootApplication
public class ChatStorageApplication {
    /**
     * Starts the Spring Boot application.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ChatStorageApplication.class, args);
    }
}