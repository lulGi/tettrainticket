package com.lul.train.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateScheduleRequest {

    @NotNull(message = "Train ID is required")
    private String trainId;

    @NotNull(message = "Route ID is required")
    private String routeId;

    @NotNull(message = "Departure time is required")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    private LocalDateTime arrivalTime;

    @NotNull(message = "Available seats is required")
    @Positive(message = "Available seats must be positive")
    private Integer availableSeats;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
}
