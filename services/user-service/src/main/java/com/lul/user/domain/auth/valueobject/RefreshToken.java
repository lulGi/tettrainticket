package com.lul.user.domain.auth.valueobject;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
public class RefreshToken {
    private static final int VALIDITY_DAYS = 7;

    private final String value;
    private final LocalDateTime expiryDate;

    private RefreshToken(String value, LocalDateTime expiryDate) {
        this.value = value;
        this.expiryDate = expiryDate;
    }

    // Factory: Generate new refresh token
    public static RefreshToken generate() {
        String tokenValue = UUID.randomUUID().toString() + UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusDays(VALIDITY_DAYS);
        return new RefreshToken(tokenValue, expiry);
    }

    // Factory: Recreate from stored values (for loading from DB)
    public static RefreshToken of(String value, LocalDateTime expiryDate) {
        return new RefreshToken(value, expiryDate);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public boolean matches(String tokenValue) {
        return Objects.equals(this.value, tokenValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshToken)) return false;
        RefreshToken that = (RefreshToken) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
