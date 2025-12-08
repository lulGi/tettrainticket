package com.lul.train.application.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateRouteRequest {

    @Positive(message = "Distance must be positive")
    private Integer distance;

    @Positive(message = "Estimated duration must be positive")
    private Integer estimatedDuration;
}
