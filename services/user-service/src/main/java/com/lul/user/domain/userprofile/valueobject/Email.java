package com.lul.user.domain.userprofile.valueobject;

import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Email {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private final String value;

    public Email(String value) {
        validate(value);
        this.value = value.toLowerCase().trim();
    }

    private void validate(String email) {
        if (email == null || email.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Email cannot be empty");
        }
        if (!email.matches(EMAIL_REGEX)) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Invalid email format");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
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
