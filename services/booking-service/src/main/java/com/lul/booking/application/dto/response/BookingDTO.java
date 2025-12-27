package com.lul.booking.application.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingDTO {
    private String id;
    private String userId;
    private String scheduleId;
    private List<PassengerDTO> passengers;
    private Integer numberOfSeats;
    private BigDecimal totalPrice;
    private String status;
    private String paymentId;
    private String cancellationReason;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}