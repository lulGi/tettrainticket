package com.lul.train.application.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStationRequest {

    @Size(max = 100)
    private String name;

    @Size(max = 50)
    private String city;

    @Size(max = 50)
    private String province;
}