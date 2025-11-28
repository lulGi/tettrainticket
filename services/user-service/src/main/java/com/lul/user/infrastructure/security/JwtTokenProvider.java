package com.lul.user.infrastructure.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * JWT Token Provider using Nimbus JOSE + JWT library.
 * Generates and validates JWT tokens with HS512 algorithm.
 */
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token-expiration}")
    @Getter
    private long accessTokenExpiration; // in milliseconds

    /**
     * Generate JWT access token with user claims
     *
     * @param userId   User's unique identifier
     * @param username User's username
     * @param roles    User's roles (e.g., USER, ADMIN)
     * @return JWT access token string
     */
    public String generateAccessToken(String userId, String username, Set<String> roles) {
        try {
            // 1. Create JWS Header with HS512 algorithm
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

            // 2. Build JWT Claims Set
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(userId)                                    // User ID
                    .issuer("tettrainticket.com")                      // Issuer
                    .issueTime(new Date())                             // Issue time
                    .expirationTime(new Date(
                            Instant.now().plus(accessTokenExpiration, ChronoUnit.MILLIS).toEpochMilli()
                    ))                                                 // Expiry time
                    .jwtID(UUID.randomUUID().toString())              // Unique JWT ID
                    .claim("username", username)                       // Username claim
                    .claim("scope", buildScope(roles))                 // Scope claim (roles)
                    .build();

            // 3. Create payload
            Payload payload = new Payload(jwtClaimsSet.toJSONObject());

            // 4. Create JWS object and sign
            JWSObject jwsObject = new JWSObject(header, payload);
            jwsObject.sign(new MACSigner(jwtSecret.getBytes()));

            // 5. Return serialized token
            return jwsObject.serialize();

        } catch (JOSEException e) {
            log.error("Error generating JWT token: {}", e.getMessage());
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    /**
     * Build scope claim from roles
     * Format: "ROLE_USER ROLE_ADMIN" (space-separated)
     *
     * @param roles Set of role names
     * @return Space-separated scope string
     */
    private String buildScope(Set<String> roles) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (roles != null && !roles.isEmpty()) {
            roles.forEach(role -> stringJoiner.add("ROLE_" + role));
        }
        return stringJoiner.toString();
    }

    /**
     * Validate JWT token signature and expiry
     *
     * @param token JWT token string
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = verifyToken(token);
            return signedJWT != null;
        } catch (Exception e) {
            log.error("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Parse and verify JWT token
     *
     * @param token JWT token string
     * @return SignedJWT object if valid
     * @throws JOSEException  if signature verification fails
     * @throws ParseException if token parsing fails
     */
    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        // 1. Create MAC verifier with secret key
        JWSVerifier verifier = new MACVerifier(jwtSecret.getBytes());

        // 2. Parse token to SignedJWT
        SignedJWT signedJWT = SignedJWT.parse(token);

        // 3. Get expiration time
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        // 4. Verify signature
        boolean verified = signedJWT.verify(verifier);

        // 5. Check: signature valid AND not expired
        if (verified && expiryTime.after(new Date())) {
            return signedJWT;
        }

        throw new JOSEException("Token verification failed: invalid signature or expired");
    }

    /**
     * Extract user ID from JWT token
     *
     * @param token JWT token string
     * @return User ID (subject claim)
     */
    public String getUserIdFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            log.error("Failed to parse token: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    /**
     * Extract username from JWT token
     *
     * @param token JWT token string
     * @return Username claim
     */
    public String getUsernameFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getStringClaim("username");
        } catch (ParseException e) {
            log.error("Failed to parse token: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    /**
     * Extract scope (roles) from JWT token
     *
     * @param token JWT token string
     * @return Scope claim (space-separated roles)
     */
    public String getScopeFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getStringClaim("scope");
        } catch (ParseException e) {
            log.error("Failed to parse token: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
}
