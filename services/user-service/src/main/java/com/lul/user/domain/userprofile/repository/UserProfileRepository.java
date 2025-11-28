package com.lul.user.domain.userprofile.repository;

import com.lul.user.domain.userprofile.aggregate.UserProfile;
import com.lul.user.domain.userprofile.valueobject.Email;

import java.util.Optional;

/**
 * Repository interface for UserProfile aggregate.
 * This is a domain layer interface (port in hexagonal architecture).
 * Implementation will be in infrastructure layer (adapter).
 */
public interface UserProfileRepository {
    UserProfile save(UserProfile userProfile);

    Optional<UserProfile> findById(String id);

    Optional<UserProfile> findByEmail(Email email);

    boolean existsByEmail(Email email);

    void deleteById(String id);
}
