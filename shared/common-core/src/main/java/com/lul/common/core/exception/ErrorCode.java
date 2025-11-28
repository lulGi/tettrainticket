package com.lul.common.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // General errors
    NOT_FOUND("NOT_FOUND", HttpStatus.NOT_FOUND),
    ALREADY_EXISTS("ALREADY_EXISTS", HttpStatus.CONFLICT),
    VALIDATION_ERROR("VALIDATION_ERROR", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),

    // Authentication errors (401 - Chưa xác thực hoặc xác thực thất bại)
    UNAUTHENTICATED("UNAUTHENTICATED", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("INVALID_TOKEN", HttpStatus.UNAUTHORIZED),

    // Authorization errors (403 - Đã xác thực nhưng không có quyền)
    FORBIDDEN("FORBIDDEN", HttpStatus.FORBIDDEN),
    ACCESS_DENIED("ACCESS_DENIED", HttpStatus.FORBIDDEN);

    private final String code;
    private final HttpStatus httpStatus;
}
