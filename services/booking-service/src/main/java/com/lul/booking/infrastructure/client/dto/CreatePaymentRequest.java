package com.lul.booking.infrastructure.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {
    private String bookingId;
    private String userId;
    private BigDecimal amount;
    private String paymentMethod;
}