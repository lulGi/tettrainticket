package com.lul.user.presentation.controller;

import com.lul.common.core.response.ApiResponse;
import com.lul.user.application.dto.request.UpdateProfileRequest;
import com.lul.user.application.dto.response.UserProfileResponse;
import com.lul.user.application.service.UserProfileApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for user profile endpoints.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileApplicationService userProfileService;

    /**
     * Get current user's profile
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getCurrentUserProfile(
            Authentication authentication) {

        String userId = authentication.getName(); // Subject from JWT
        UserProfileResponse response = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(
                ApiResponse.success(response));
    }

    /**
     * Update current user's profile
     */
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateCurrentUserProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {

        String userId = authentication.getName(); // Subject from JWT
        UserProfileResponse response = userProfileService.updateProfile(request, userId);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Profile updated successfully"));
    }

    /**
     * Get user profile by ID (for admin or public access)
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfileById(
            @PathVariable String userId) {

        UserProfileResponse response = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(
                ApiResponse.success(response));
    }
}
