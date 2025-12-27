package com.lul.user.domain.auth.repository;

import com.lul.user.domain.auth.aggregate.Credential;

import java.util.Optional;

/**
 * Repository interface for Credential aggregate.
 * This is a domain layer interface (port in hexagonal architecture).
 * Implementation will be in infrastructure layer (adapter).
 */
public interface CredentialRepository {
    Credential save(Credential credential);

    Optional<Credential> findById(String id);

    Optional<Credential> findByUsername(String username);

    Optional<Credential> findByUserProfileId(String userProfileId);

    Optional<Credential> findByRefreshToken(String refreshToken);

    boolean existsByUsername(String username);

    void deleteById(String id);
}
