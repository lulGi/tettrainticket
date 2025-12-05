package com.lul.train.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStationRequest {

    @NotBlank(message = "Station code is required")
    @Pattern(regexp = "^[A-Z]{2,5}$", message = "Station code must be 2-5 uppercase letters")
    private String stationCode;

    @NotBlank(message = "Station name is required")
    @Size(max = 100, message = "Station name too long")
    private String name;

    @Size(max = 50)
    private String city;

    @Size(max = 50)
    private String province;
}