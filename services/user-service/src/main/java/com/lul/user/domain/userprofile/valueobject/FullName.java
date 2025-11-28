package com.lul.user.domain.userprofile.valueobject;

import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import lombok.Getter;

import java.util.Objects;

@Getter
public class FullName {
    private final String firstName;
    private final String lastName;

    public FullName(String firstName, String lastName) {
        validate(firstName, lastName);
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
    }

    private void validate(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "First name cannot be empty");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Last name cannot be empty");
        }
        if (firstName.length() > 100) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "First name too long (max 100 characters)");
        }
        if (lastName.length() > 100) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Last name too long (max 100 characters)");
        }
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FullName)) return false;
        FullName fullName = (FullName) o;
        return Objects.equals(firstName, fullName.firstName) &&
                Objects.equals(lastName, fullName.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return getFullName();
    }
}
