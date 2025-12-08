package com.lul.train.application.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UpdateScheduleRequest {

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    @Positive(message = "Available seats must be positive")
    private Integer availableSeats;

    @Positive(message = "Price must be positive")
    private BigDecimal price;
}
