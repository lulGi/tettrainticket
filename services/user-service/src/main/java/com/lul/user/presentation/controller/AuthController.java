package com.lul.user.presentation.controller;

import com.lul.common.core.response.ApiResponse;
import com.lul.user.application.dto.request.ChangePasswordRequest;
import com.lul.user.application.dto.request.LoginRequest;
import com.lul.user.application.dto.request.RefreshTokenRequest;
import com.lul.user.application.dto.request.RegisterUserRequest;
import com.lul.user.application.dto.response.AuthResponse;
import com.lul.user.application.dto.response.TokenResponse;
import com.lul.user.application.service.AuthenticationApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for authentication endpoints.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationApplicationService authService;

    /**
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterUserRequest request) {

        AuthResponse response = authService.registerUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "User registered successfully"));
    }

    /**
     * User login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Login successful"));
    }

    /**
     * Refresh access token
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {

        TokenResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Token refreshed successfully"));
    }

    /**
     * Change password (protected endpoint)
     */
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {

        String userId = authentication.getName(); // Subject from JWT
        authService.changePassword(request, userId);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Password changed successfully"));
    }

    /**
     * Deactivate account (protected endpoint)
     */
    @PostMapping("/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivate(Authentication authentication) {

        String userId = authentication.getName(); // Subject from JWT
        authService.deactivateAccount(userId);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Account deactivated successfully"));
    }
}
