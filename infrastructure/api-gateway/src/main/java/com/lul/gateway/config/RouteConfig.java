package com.lul.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // User Service routes - Port 8081
                .route("user-service", r -> r
                        .path("/api/users/**", "/api/auth/**")
                        .filters(f -> f
                                .stripPrefix(1) // Remove /api prefix before forwarding
                                .addRequestHeader("X-Gateway", "api-gateway")
                        )
                        .uri("lb://user-service") // Load balanced URI from Eureka
                )
                
                // Train Service routes - Port 8082
                .route("train-service", r -> r
                        .path("/api/trains/**", "/api/stations/**", "/api/routes/**", "/api/schedules/**")
                        .filters(f -> f
                                .stripPrefix(1) // Remove /api prefix before forwarding
                                .addRequestHeader("X-Gateway", "api-gateway")
                        )
                        .uri("lb://train-service")
                )
                
                // Payment Service routes - Port 8083
                .route("payment-service", r -> r
                        .path("/api/payments/**")
                        .filters(f -> f
                                .stripPrefix(1) // Remove /api prefix before forwarding
                                .addRequestHeader("X-Gateway", "api-gateway")
                        )
                        .uri("lb://payment-service")
                )
                
                // Booking Service routes - Port 8084
                .route("booking-service", r -> r
                        .path("/api/bookings/**")
                        .filters(f -> f
                                .stripPrefix(1) // Remove /api prefix before forwarding
                                .addRequestHeader("X-Gateway", "api-gateway")
                        )
                        .uri("lb://booking-service")
                )
                
                // Notification Service routes - Port 8085
                .route("notification-service", r -> r
                        .path("/api/notifications/**")
                        .filters(f -> f
                                .stripPrefix(1) // Remove /api prefix before forwarding
                                .addRequestHeader("X-Gateway", "api-gateway")
                        )
                        .uri("lb://notification-service")
                )
                
                .build();
    }
}
