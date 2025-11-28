package com.lul.user.domain.userprofile.valueobject;

import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import lombok.Getter;

import java.util.Objects;

@Getter
public class PhoneNumber {
    private static final String PHONE_REGEX = "^\\+?[1-9]\\d{1,14}$"; // E.164 format

    private final String value;

    public PhoneNumber(String value) {
        if (value != null && !value.isBlank()) {
            validate(value);
            this.value = value.trim();
        } else {
            this.value = null;
        }
    }

    private void validate(String phone) {
        if (!phone.matches(PHONE_REGEX)) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Invalid phone number format. Use E.164 format (e.g., +84912345678)");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneNumber)) return false;
        PhoneNumber that = (PhoneNumber) o;
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
