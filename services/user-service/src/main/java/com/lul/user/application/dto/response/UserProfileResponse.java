package com.lul.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for user profile information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    private String id;

    private String username;

    private String email;

    private String fullName;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private LocalDateTime createdAt;
}
