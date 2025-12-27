package com.lul.booking.domain.booking.valueobject;

import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Passenger {
    private final String fullName;
    private final String identityNumber;  // CMND/CCCD
    private final String phoneNumber;

    private Passenger(String fullName, String identityNumber, String phoneNumber) {
        this.fullName = fullName;
        this.identityNumber = identityNumber;
        this.phoneNumber = phoneNumber;
    }

    public static Passenger create(String fullName, String identityNumber, String phoneNumber) {
        validateFullName(fullName);
        validateIdentityNumber(identityNumber);
        validatePhoneNumber(phoneNumber);
        return new Passenger(fullName, identityNumber, phoneNumber);
    }

    private static void validateFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Passenger full name cannot be empty");
        }
        if (fullName.length() > 100) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Passenger full name too long");
        }
    }

    private static void validateIdentityNumber(String identityNumber) {
        if (identityNumber == null || identityNumber.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Identity number cannot be empty");
        }
        if (!identityNumber.matches("\\d{9}|\\d{12}")) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Identity number must be 9 or 12 digits");
        }
    }

    private static void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Phone number cannot be empty");
        }
        if (!phoneNumber.matches("^0\\d{9}$")) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Phone number must be 10 digits starting with 0");
        }
    }
}