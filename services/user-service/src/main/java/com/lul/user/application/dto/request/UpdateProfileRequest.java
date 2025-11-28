package com.lul.user.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Request DTO for updating user profile.
 * All fields are optional.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private LocalDate dateOfBirth;
}
