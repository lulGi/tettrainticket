package com.lul.user.infrastructure.persistence.repository;

import com.lul.user.domain.auth.aggregate.Credential;
import com.lul.user.domain.auth.repository.CredentialRepository;
import com.lul.user.infrastructure.persistence.entity.CredentialEntity;
import com.lul.user.infrastructure.persistence.mapper.CredentialMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Infrastructure implementation of CredentialRepository.
 * This is an adapter in hexagonal architecture that adapts the domain repository interface
 * to the JPA repository implementation.
 */
@Repository
@RequiredArgsConstructor
public class CredentialRepositoryImpl implements CredentialRepository {

    private final CredentialJpaRepository jpaRepository;
    private final CredentialMapper mapper;

    @Override
    public Credential save(Credential credential) {
        CredentialEntity entity = mapper.toEntity(credential);
        CredentialEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Credential> findById(String id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<Credential> findByUsername(String username) {
        return jpaRepository.findByUsername(username)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<Credential> findByUserProfileId(String userProfileId) {
        return jpaRepository.findByUserProfileId(userProfileId)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<Credential> findByRefreshToken(String refreshToken) {
        return jpaRepository.findByRefreshToken(refreshToken)
            .map(mapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
