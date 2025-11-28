package com.lul.user.infrastructure.persistence.mapper;

import com.lul.user.domain.auth.aggregate.Credential;
import com.lul.user.domain.auth.valueobject.Password;
import com.lul.user.domain.auth.valueobject.RefreshToken;
import com.lul.user.infrastructure.persistence.entity.CredentialEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CredentialMapper {

    @Mapping(target = "userProfileId", source = "userProfileId")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "passwordHash", expression = "java(domain.getPassword().getHashedValue())")
    @Mapping(target = "refreshToken", expression = "java(domain.getRefreshToken() != null ? domain.getRefreshToken().getValue() : null)")
    @Mapping(target = "refreshTokenExpiry", expression = "java(domain.getRefreshToken() != null ? domain.getRefreshToken().getExpiryDate() : null)")
    @Mapping(target = "roles", expression = "java(rolesToString(domain.getRoles()))")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "lastLoginAt", source = "lastLoginAt")
    CredentialEntity toEntity(Credential domain);

    default Credential toDomain(CredentialEntity entity) {
        if (entity == null) {
            return null;
        }

        // 1. Reconstruct Password Value Object
        Password password = Password.fromHashed(entity.getPasswordHash());

        // 2. Create Credential using factory method (không dùng constructor)
        Credential credential = Credential.create(
            entity.getUserProfileId(),
            entity.getUsername(),
            password
        );

        // 3. Set RefreshToken if exists (using reflection vì field private)
        if (entity.getRefreshToken() != null && entity.getRefreshTokenExpiry() != null) {
            try {
                java.lang.reflect.Field refreshTokenField = Credential.class.getDeclaredField("refreshToken");
                refreshTokenField.setAccessible(true);
                RefreshToken refreshToken = RefreshToken.of(
                    entity.getRefreshToken(),
                    entity.getRefreshTokenExpiry()
                );
                refreshTokenField.set(credential, refreshToken);
            } catch (Exception e) {
                throw new RuntimeException("Failed to set refresh token", e);
            }
        }

        // 4. Set isActive (using reflection)
        if (!entity.isActive()) {
            try {
                java.lang.reflect.Field isActiveField = Credential.class.getDeclaredField("isActive");
                isActiveField.setAccessible(true);
                isActiveField.set(credential, entity.isActive());
            } catch (Exception e) {
                throw new RuntimeException("Failed to set isActive", e);
            }
        }

        // 5. Set lastLoginAt (using reflection)
        if (entity.getLastLoginAt() != null) {
            try {
                java.lang.reflect.Field lastLoginAtField = Credential.class.getDeclaredField("lastLoginAt");
                lastLoginAtField.setAccessible(true);
                lastLoginAtField.set(credential, entity.getLastLoginAt());
            } catch (Exception e) {
                throw new RuntimeException("Failed to set lastLoginAt", e);
            }
        }

        // 6. Set roles (using reflection)
        try {
            java.lang.reflect.Field rolesField = Credential.class.getDeclaredField("roles");
            rolesField.setAccessible(true);
            rolesField.set(credential, stringToRoles(entity.getRoles()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to set roles", e);
        }

        // 7. Set BaseEntity fields (id, version, createdAt, updatedAt)
        setBaseEntityFields(credential, entity);

        return credential;
    }

    /**
     * Convert Set<Role> to comma-separated String
     */
    default String rolesToString(java.util.Set<com.lul.user.domain.auth.valueobject.Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return "USER"; // Default
        }
        return roles.stream()
                .map(Enum::name)
                .collect(java.util.stream.Collectors.joining(","));
    }

    /**
     * Convert comma-separated String to Set<Role>
     */
    default java.util.Set<com.lul.user.domain.auth.valueobject.Role> stringToRoles(String rolesStr) {
        if (rolesStr == null || rolesStr.trim().isEmpty()) {
            return java.util.Set.of(com.lul.user.domain.auth.valueobject.Role.USER);
        }
        return java.util.Arrays.stream(rolesStr.split(","))
                .map(String::trim)
                .map(com.lul.user.domain.auth.valueobject.Role::valueOf)
                .collect(java.util.stream.Collectors.toSet());
    }

    default void setBaseEntityFields(Credential domain, CredentialEntity entity) {
        try {
            // Set id
            java.lang.reflect.Field idField = com.lul.common.core.domain.BaseEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(domain, entity.getId());

            // Set version
            java.lang.reflect.Field versionField = com.lul.common.core.domain.BaseEntity.class.getDeclaredField("version");
            versionField.setAccessible(true);
            versionField.set(domain, entity.getVersion());

            // Set createdAt
            java.lang.reflect.Field createdAtField = com.lul.common.core.domain.BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(domain, entity.getCreatedAt());

            // Set updatedAt
            java.lang.reflect.Field updatedAtField = com.lul.common.core.domain.BaseEntity.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(domain, entity.getUpdatedAt());
        } catch (Exception e) {
            throw new RuntimeException("Failed to set BaseEntity fields", e);
        }
    }
}
