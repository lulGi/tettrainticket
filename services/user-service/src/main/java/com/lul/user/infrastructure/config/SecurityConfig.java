package com.lul.user.infrastructure.config;

import com.lul.user.infrastructure.security.CustomJwtDecoder;
import com.lul.user.infrastructure.security.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for User Service.
 * Configures JWT-based authentication with OAuth2 Resource Server.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomJwtDecoder customJwtDecoder;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/register",
            "/api/auth/login",
            "/api/auth/refresh"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 1. Configure endpoint authorization
        httpSecurity.authorizeHttpRequests(request ->
                request
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
        );

        // 2. Configure OAuth2 Resource Server with custom JWT decoder
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );

        // 3. Disable CSRF (stateless JWT)
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    /**
     * Convert JWT claims to Spring Security authorities.
     * Reads "scope" claim and converts to GrantedAuthority.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
                new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(""); // No "SCOPE_" prefix

        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                jwtGrantedAuthoritiesConverter
        );

        return jwtAuthenticationConverter;
    }
}
