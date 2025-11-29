package com.lul.user.infrastructure.persistence.entity;

import com.lul.common.core.infrastructure.persistence.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * JPA Entity for user credentials - Infrastructure layer
 */
@Entity
@Table(name = "user_credentials")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CredentialEntity extends BaseJpaEntity {

    @Column(name = "user_profile_id", nullable = false, unique = true, length = 36)
    private String userProfileId;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    @Column(name = "refresh_token_expiry")
    private LocalDateTime refreshTokenExpiry;

    @Column(name = "roles", nullable = false, length = 255)
    private String roles; // Comma-separated role names (e.g., "USER,ADMIN")

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
}
