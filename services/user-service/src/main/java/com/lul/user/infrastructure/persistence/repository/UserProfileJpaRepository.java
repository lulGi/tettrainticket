package com.lul.user.infrastructure.persistence.repository;

import com.lul.user.infrastructure.persistence.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileJpaRepository extends JpaRepository<UserProfileEntity, String> {
    Optional<UserProfileEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
