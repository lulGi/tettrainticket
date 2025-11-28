package com.lul.user.infrastructure.persistence.repository;

import com.lul.user.domain.userprofile.aggregate.UserProfile;
import com.lul.user.domain.userprofile.repository.UserProfileRepository;
import com.lul.user.domain.userprofile.valueobject.Email;
import com.lul.user.infrastructure.persistence.entity.UserProfileEntity;
import com.lul.user.infrastructure.persistence.mapper.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Infrastructure implementation of UserProfileRepository.
 * This is an adapter in hexagonal architecture that adapts the domain repository interface
 * to the JPA repository implementation.
 */
@Repository
@RequiredArgsConstructor
public class UserProfileRepositoryImpl implements UserProfileRepository {

    private final UserProfileJpaRepository jpaRepository;
    private final UserProfileMapper mapper;

    @Override
    public UserProfile save(UserProfile userProfile) {
        UserProfileEntity entity = mapper.toEntity(userProfile);
        UserProfileEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<UserProfile> findById(String id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<UserProfile> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.getValue())
            .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.getValue());
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
