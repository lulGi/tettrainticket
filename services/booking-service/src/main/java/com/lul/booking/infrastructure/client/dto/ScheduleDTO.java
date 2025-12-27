package com.lul.booking.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO from train-service Schedule API
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleDTO {
    private String id;
    private String trainId;
    private String routeId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer availableSeats;
    private BigDecimal price;
    private String status;
}