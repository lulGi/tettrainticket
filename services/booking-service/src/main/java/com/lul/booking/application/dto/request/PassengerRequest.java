package com.lul.booking.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PassengerRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Identity number is required")
    @Pattern(regexp = "\\d{9}|\\d{12}", message = "Identity number must be 9 or 12 digits")
    private String identityNumber;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^0\\d{9}$", message = "Phone number must be 10 digits starting with 0")
    private String phoneNumber;

    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
}