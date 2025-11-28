package com.lul.user.domain.userprofile.aggregate;

import com.lul.common.core.domain.AggregateRoot;
import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import com.lul.user.domain.userprofile.valueobject.Email;
import com.lul.user.domain.userprofile.valueobject.FullName;
import com.lul.user.domain.userprofile.valueobject.PhoneNumber;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserProfile extends AggregateRoot<String> {
    private Email email;
    private FullName fullName;
    private PhoneNumber phoneNumber;
    private LocalDate dateOfBirth;

    // Private constructor for domain logic
    private UserProfile(Email email, FullName fullName, PhoneNumber phoneNumber, LocalDate dateOfBirth) {
        super();
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    // Factory method - create new user profile
    public static UserProfile create(Email email, FullName fullName, PhoneNumber phoneNumber, LocalDate dateOfBirth) {
        validateDateOfBirth(dateOfBirth);
        return new UserProfile(email, fullName, phoneNumber, dateOfBirth);
    }

    // Domain behavior - update profile information
    public void updateProfile(FullName newName, PhoneNumber newPhone, LocalDate newDob) {
        if (newName != null) {
            this.fullName = newName;
        }
        if (newPhone != null) {
            this.phoneNumber = newPhone;
        }
        if (newDob != null) {
            validateDateOfBirth(newDob);
            this.dateOfBirth = newDob;
        }
    }

    private static void validateDateOfBirth(LocalDate dob) {
        if (dob != null && dob.isAfter(LocalDate.now().minusYears(13))) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "User must be at least 13 years old");
        }
    }

    // Email cannot be changed (business rule)
    // If needed in future, create separate "changeEmail" method with email verification process
}
