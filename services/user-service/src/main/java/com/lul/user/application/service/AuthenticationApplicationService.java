package com.lul.user.application.service;

import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import com.lul.user.application.dto.mapper.UserDtoMapper;
import com.lul.user.application.dto.request.ChangePasswordRequest;
import com.lul.user.application.dto.request.LoginRequest;
import com.lul.user.application.dto.request.RefreshTokenRequest;
import com.lul.user.application.dto.request.RegisterUserRequest;
import com.lul.user.application.dto.response.AuthResponse;
import com.lul.user.application.dto.response.TokenResponse;
import com.lul.user.application.dto.response.UserProfileResponse;
import com.lul.user.domain.auth.aggregate.Credential;
import com.lul.user.domain.auth.repository.CredentialRepository;
import com.lul.user.domain.auth.valueobject.Password;
import com.lul.user.domain.userprofile.aggregate.UserProfile;
import com.lul.user.domain.userprofile.repository.UserProfileRepository;
import com.lul.user.domain.userprofile.valueobject.Email;
import com.lul.user.domain.userprofile.valueobject.FullName;
import com.lul.user.domain.userprofile.valueobject.PhoneNumber;
import com.lul.user.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * Application service for authentication and credential management operations.
 * Orchestrates domain operations and handles cross-aggregate transactions.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationApplicationService {

    private final UserProfileRepository userProfileRepository;
    private final CredentialRepository credentialRepository;
    private final Password.PasswordHasher passwordHasher;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDtoMapper dtoMapper;

    /**
     * Register a new user (creates both UserProfile and Credential aggregates)
     *
     * @param request Registration request with user details
     * @return AuthResponse with tokens and user profile
     */
    @Transactional
    public AuthResponse registerUser(RegisterUserRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        // 1. Check username uniqueness
        if (credentialRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }

        // 2. Check email uniqueness
        Email email = new Email(request.getEmail());
        if (userProfileRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 3. Create UserProfile aggregate
        UserProfile userProfile = UserProfile.create(
                email,
                new FullName(request.getFirstName(), request.getLastName()),
                request.getPhoneNumber() != null ? new PhoneNumber(request.getPhoneNumber()) : null,
                request.getDateOfBirth()
        );

        // 4. Save UserProfile
        userProfile = userProfileRepository.save(userProfile);

        // 5. Create Credential aggregate (default role: USER)
        Password password = Password.fromRaw(request.getPassword(), passwordHasher);
        Credential credential = Credential.create(
                userProfile.getId(),
                request.getUsername(),
                password
        );

        // 6. Generate refresh token
        credential.generateRefreshToken();

        // 7. Save Credential
        credential = credentialRepository.save(credential);

        // 8. Generate access token with roles
        String accessToken = jwtTokenProvider.generateAccessToken(
                userProfile.getId(),
                credential.getUsername(),
                credential.getRoles().stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet())
        );

        // 9. Build response
        UserProfileResponse profileResponse = dtoMapper.toUserProfileResponse(
                userProfile,
                credential.getUsername()
        );

        log.info("User registered successfully: {}", credential.getUsername());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(credential.getRefreshToken().getValue())
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenExpiration())
                .userProfile(profileResponse)
                .build();
    }

    /**
     * Authenticate user and generate tokens
     *
     * @param request Login request with username and password
     * @return AuthResponse with tokens and user profile
     */
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("User login attempt: {}", request.getUsername());

        // 1. Find credential by username
        Credential credential = credentialRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        // 2. Authenticate (domain behavior validates password and updates lastLoginAt)
        credential.authenticate(request.getPassword(), passwordHasher);

        // 3. Generate new refresh token
        credential.generateRefreshToken();

        // 4. Save updated credential
        credential = credentialRepository.save(credential);

        // 5. Get user profile
        UserProfile userProfile = userProfileRepository.findById(credential.getUserProfileId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_PROFILE_NOT_FOUND));

        // 6. Generate access token with roles
        String accessToken = jwtTokenProvider.generateAccessToken(
                userProfile.getId(),
                credential.getUsername(),
                credential.getRoles().stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet())
        );

        // 7. Build response
        UserProfileResponse profileResponse = dtoMapper.toUserProfileResponse(
                userProfile,
                credential.getUsername()
        );

        log.info("User logged in successfully: {}", credential.getUsername());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(credential.getRefreshToken().getValue())
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenExpiration())
                .userProfile(profileResponse)
                .build();
    }

    /**
     * Refresh access token using refresh token
     *
     * @param request Refresh token request
     * @return TokenResponse with new access and refresh tokens
     */
    @Transactional
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        log.info("Token refresh attempt with refresh token");

        // 1. Find credential by refresh token
        Credential credential = credentialRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_TOKEN, "Invalid refresh token"));

        // 2. Validate and rotate refresh token (domain behavior)
        credential.validateAndRotateRefreshToken(request.getRefreshToken());

        // 3. Save updated credential with new refresh token
        credential = credentialRepository.save(credential);

        // 4. Generate new access token with roles
        String accessToken = jwtTokenProvider.generateAccessToken(
                credential.getUserProfileId(),
                credential.getUsername(),
                credential.getRoles().stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet())
        );

        log.info("Token refreshed successfully for user: {}", credential.getUserProfileId());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(credential.getRefreshToken().getValue())
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenExpiration())
                .build();
    }

    /**
     * Change user password
     *
     * @param request Change password request
     * @param userId  Current user's ID (from JWT)
     */
    @Transactional
    public void changePassword(ChangePasswordRequest request, String userId) {
        log.info("Password change attempt for user: {}", userId);

        // 1. Find credential by user profile ID
        Credential credential = credentialRepository.findByUserProfileId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREDENTIAL_NOT_FOUND));

        // 2. Change password (domain behavior validates old password and invalidates refresh token)
        credential.changePassword(
                request.getCurrentPassword(),
                request.getNewPassword(),
                passwordHasher
        );

        // 3. Save updated credential
        credentialRepository.save(credential);

        log.info("Password changed successfully for user: {}", userId);
    }

    /**
     * Deactivate user account
     *
     * @param userId Current user's ID (from JWT)
     */
    @Transactional
    public void deactivateAccount(String userId) {
        log.info("Account deactivation attempt for user: {}", userId);

        // 1. Find credential by user profile ID
        Credential credential = credentialRepository.findByUserProfileId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREDENTIAL_NOT_FOUND));

        // 2. Deactivate (domain behavior clears refresh token)
        credential.deactivate();

        // 3. Save updated credential
        credentialRepository.save(credential);

        log.info("Account deactivated successfully for user: {}", userId);
    }
}
