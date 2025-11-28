package com.lul.user.domain.auth.valueobject;

import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Password {
    private static final int MIN_LENGTH = 8;

    private final String hashedValue;

    // Private constructor - use factory methods
    private Password(String hashedValue) {
        this.hashedValue = hashedValue;
    }

    // Factory: Create from raw password (for registration/password change)
    // Note: PasswordEncoder should be injected from infrastructure layer
    public static Password fromRaw(String rawPassword, PasswordHasher hasher) {
        validateStrength(rawPassword);
        String hashed = hasher.hash(rawPassword);
        return new Password(hashed);
    }

    // Factory: Create from hashed value (for loading from DB)
    public static Password fromHashed(String hashedValue) {
        return new Password(hashedValue);
    }

    private static void validateStrength(String raw) {
        if (raw == null || raw.length() < MIN_LENGTH) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Password must be at least " + MIN_LENGTH + " characters");
        }
        if (!raw.matches(".*[A-Z].*")) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Password must contain at least one uppercase letter");
        }
        if (!raw.matches(".*[a-z].*")) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Password must contain at least one lowercase letter");
        }
        if (!raw.matches(".*\\d.*")) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Password must contain at least one digit");
        }
    }

    public boolean matches(String rawPassword, PasswordHasher hasher) {
        return hasher.matches(rawPassword, this.hashedValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Password)) return false;
        Password password = (Password) o;
        return Objects.equals(hashedValue, password.hashedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashedValue);
    }

    // Domain service interface for password hashing
    // Implementation will be in infrastructure layer
    public interface PasswordHasher {
        String hash(String rawPassword);
        boolean matches(String rawPassword, String hashedPassword);
    }
}
