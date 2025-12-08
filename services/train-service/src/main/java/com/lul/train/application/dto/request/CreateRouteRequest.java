package com.lul.train.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateRouteRequest {

    @NotNull(message = "Route code is required")
    private String routeCode;

    @NotNull(message = "Origin station ID is required")
    private String originStationId;

    @NotNull(message = "Destination station ID is required")
    private String destinationStationId;

    @NotNull(message = "Distance is required")
    @Positive(message = "Distance must be positive")
    private Integer distance;

    @NotNull(message = "Estimated duration is required")
    @Positive(message = "Estimated duration must be positive")
    private Integer estimatedDuration;
}
