package com.lul.user.infrastructure.persistence.repository;

import com.lul.user.infrastructure.persistence.entity.CredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialJpaRepository extends JpaRepository<CredentialEntity, String> {
    Optional<CredentialEntity> findByUsername(String username);

    Optional<CredentialEntity> findByUserProfileId(String userProfileId);

    Optional<CredentialEntity> findByRefreshToken(String refreshToken);

    boolean existsByUsername(String username);
}
