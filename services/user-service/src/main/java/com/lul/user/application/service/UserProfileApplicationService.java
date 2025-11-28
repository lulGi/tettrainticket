package com.lul.user.application.service;

import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import com.lul.user.application.dto.mapper.UserDtoMapper;
import com.lul.user.application.dto.request.UpdateProfileRequest;
import com.lul.user.application.dto.response.UserProfileResponse;
import com.lul.user.domain.auth.aggregate.Credential;
import com.lul.user.domain.auth.repository.CredentialRepository;
import com.lul.user.domain.userprofile.aggregate.UserProfile;
import com.lul.user.domain.userprofile.repository.UserProfileRepository;
import com.lul.user.domain.userprofile.valueobject.FullName;
import com.lul.user.domain.userprofile.valueobject.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service for user profile operations.
 * Handles profile queries and updates.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileApplicationService {

    private final UserProfileRepository userProfileRepository;
    private final CredentialRepository credentialRepository;
    private final UserDtoMapper dtoMapper;

    /**
     * Get user profile by user ID
     *
     * @param userId User's ID
     * @return UserProfileResponse with profile details
     */
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(String userId) {
        log.info("Getting user profile for user: {}", userId);

        // 1. Find user profile
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_PROFILE_NOT_FOUND));

        // 2. Find credential to get username
        Credential credential = credentialRepository.findByUserProfileId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREDENTIAL_NOT_FOUND));

        // 3. Map to response DTO
        return dtoMapper.toUserProfileResponse(userProfile, credential.getUsername());
    }

    /**
     * Update user profile
     *
     * @param request UpdateProfileRequest with new profile data
     * @param userId  Current user's ID (from JWT)
     * @return Updated UserProfileResponse
     */
    @Transactional
    public UserProfileResponse updateProfile(UpdateProfileRequest request, String userId) {
        log.info("Updating profile for user: {}", userId);

        // 1. Find user profile
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_PROFILE_NOT_FOUND));

        // 2. Prepare new values (use existing if not provided)
        FullName newFullName = null;
        if (request.getFirstName() != null && request.getLastName() != null) {
            newFullName = new FullName(request.getFirstName(), request.getLastName());
        }

        PhoneNumber newPhoneNumber = null;
        if (request.getPhoneNumber() != null) {
            newPhoneNumber = new PhoneNumber(request.getPhoneNumber());
        }

        // 3. Update profile (domain behavior)
        userProfile.updateProfile(
                newFullName,
                newPhoneNumber,
                request.getDateOfBirth()
        );

        // 4. Save updated profile
        userProfile = userProfileRepository.save(userProfile);

        // 5. Get credential for username
        Credential credential = credentialRepository.findByUserProfileId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREDENTIAL_NOT_FOUND));

        log.info("Profile updated successfully for user: {}", userId);

        // 6. Map to response DTO
        return dtoMapper.toUserProfileResponse(userProfile, credential.getUsername());
    }
}
