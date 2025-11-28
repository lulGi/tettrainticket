package com.lul.user.domain.auth.aggregate;

import com.lul.common.core.domain.AggregateRoot;
import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import com.lul.user.domain.auth.valueobject.Password;
import com.lul.user.domain.auth.valueobject.RefreshToken;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Credential extends AggregateRoot<String> {
    private String userProfileId;
    private String username;
    private Password password;
    private RefreshToken refreshToken;
    private boolean isActive;
    private LocalDateTime lastLoginAt;

    // Private constructor for domain logic
    private Credential(String userProfileId, String username, Password password) {
        super();
        this.userProfileId = userProfileId;
        this.username = username;
        this.password = password;
        this.isActive = true;
    }

    // Factory: Create credential for new user
    public static Credential create(String userProfileId, String username, Password password) {
        validateUsername(username);
        return new Credential(userProfileId, username, password);
    }

    private static void validateUsername(String username) {
        if (username == null || username.length() < 3 || username.length() > 50) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Username must be between 3 and 50 characters");
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Username can only contain letters, numbers, and underscore");
        }
    }

    // Domain behavior: Authenticate user
    public void authenticate(String rawPassword, Password.PasswordHasher hasher) {
        if (!isActive) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Account is deactivated");
        }
        if (!password.matches(rawPassword, hasher)) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS, "Invalid credentials");
        }
        this.lastLoginAt = LocalDateTime.now();
    }

    // Domain behavior: Generate new refresh token
    public String generateRefreshToken() {
        this.refreshToken = RefreshToken.generate();
        return this.refreshToken.getValue();
    }

    // Domain behavior: Validate and rotate refresh token
    public void validateAndRotateRefreshToken(String tokenValue) {
        if (refreshToken == null || !refreshToken.matches(tokenValue)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN, "Invalid refresh token");
        }
        if (refreshToken.isExpired()) {
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED, "Refresh token expired");
        }
        // Rotate: generate new token for security
        this.refreshToken = RefreshToken.generate();
    }

    // Domain behavior: Change password
    public void changePassword(String oldRawPassword, String newRawPassword, Password.PasswordHasher hasher) {
        if (!password.matches(oldRawPassword, hasher)) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Current password is incorrect");
        }
        this.password = Password.fromRaw(newRawPassword, hasher);
        // Invalidate refresh token on password change for security
        this.refreshToken = null;
    }

    // Domain behavior: Deactivate account
    public void deactivate() {
        this.isActive = false;
        this.refreshToken = null;
    }

    // Domain behavior: Reactivate account
    public void activate() {
        this.isActive = true;
    }
}
