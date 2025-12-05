package com.lul.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * User Service Application - Main Entry Point
 *
 * This service handles two bounded contexts:
 * 1. Auth - Authentication, credentials, JWT tokens
 * 2. UserProfile - User profile information management
 *
 * Architecture: DDD (Domain-Driven Design) with Clean Architecture
 * - Domain: Pure business logic (framework-agnostic)
 * - Application: Use cases orchestration
 * - Infrastructure: JPA, Security, JWT implementation
 * - Presentation: REST API controllers
 *
 * Note: JPA Auditing is configured in JpaConfig.java
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
