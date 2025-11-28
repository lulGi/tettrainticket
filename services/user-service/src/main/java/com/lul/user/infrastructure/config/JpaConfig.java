package com.lul.user.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA configuration for User Service.
 * Enables JPA auditing for automatic createdAt and updatedAt timestamps.
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.lul.user.infrastructure.persistence.repository")
public class JpaConfig {
}
