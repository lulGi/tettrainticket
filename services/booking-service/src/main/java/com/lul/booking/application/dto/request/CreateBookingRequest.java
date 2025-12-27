package com.lul.booking.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateBookingRequest {

    @NotBlank(message = "Schedule ID is required")
    private String scheduleId;

    @NotEmpty(message = "Passengers list cannot be empty")
    @Valid
    private List<PassengerRequest> passengers;

    @Min(value = 1, message = "Number of seats must be at least 1")
    @Max(value = 10, message = "Cannot book more than 10 seats")
    private Integer numberOfSeats;

    private String paymentMethod; // CREDIT_CARD, BANK_TRANSFER, etc.
}