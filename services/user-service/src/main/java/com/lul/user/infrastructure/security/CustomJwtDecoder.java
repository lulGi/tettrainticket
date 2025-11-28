package com.lul.user.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

/**
 * Custom JWT Decoder for Spring Security OAuth2 Resource Server.
 * Uses Nimbus JWT decoder with HS512 algorithm.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private NimbusJwtDecoder nimbusJwtDecoder;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            // Lazy-initialize NimbusJwtDecoder with HS512
            if (Objects.isNull(nimbusJwtDecoder)) {
                SecretKeySpec secretKeySpec = new SecretKeySpec(
                        jwtSecret.getBytes(),
                        "HS512"
                );
                nimbusJwtDecoder = NimbusJwtDecoder
                        .withSecretKey(secretKeySpec)
                        .macAlgorithm(MacAlgorithm.HS512)
                        .build();
            }

            // Decode and verify signature
            return nimbusJwtDecoder.decode(token);

        } catch (JwtException e) {
            log.error("JWT decoding failed: {}", e.getMessage());
            throw e;
        }
    }
}
