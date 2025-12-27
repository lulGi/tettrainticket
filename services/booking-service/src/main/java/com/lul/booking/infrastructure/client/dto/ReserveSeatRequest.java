package com.lul.booking.infrastructure.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for train-service reserve/release seats API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveSeatRequest {
    private Integer numberOfSeats;
}
