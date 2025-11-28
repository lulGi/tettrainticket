package com.lul.common.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // General errors
    NOT_FOUND("NOT_FOUND", "Resource not found", HttpStatus.NOT_FOUND),
    ALREADY_EXISTS("ALREADY_EXISTS", "Resource already exists", HttpStatus.CONFLICT),
    VALIDATION_ERROR("VALIDATION_ERROR", "Validation failed", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR),

    // Authentication errors (401 - Chưa xác thực hoặc xác thực thất bại)
    UNAUTHENTICATED("UNAUTHENTICATED", "Authentication required", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "Invalid username or password", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("TOKEN_EXPIRED", "Token has expired", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("INVALID_TOKEN", "Invalid token", HttpStatus.UNAUTHORIZED),

    // User-specific errors
    USERNAME_ALREADY_EXISTS("USERNAME_ALREADY_EXISTS", "Username already exists", HttpStatus.CONFLICT),
    EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS", "Email already exists", HttpStatus.CONFLICT),
    USER_PROFILE_NOT_FOUND("USER_PROFILE_NOT_FOUND", "User profile not found", HttpStatus.NOT_FOUND),
    CREDENTIAL_NOT_FOUND("CREDENTIAL_NOT_FOUND", "Credential not found", HttpStatus.NOT_FOUND),

    // Authorization errors (403 - Đã xác thực nhưng không có quyền)
    FORBIDDEN("FORBIDDEN", "Access forbidden", HttpStatus.FORBIDDEN),
    ACCESS_DENIED("ACCESS_DENIED", "Access denied", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
