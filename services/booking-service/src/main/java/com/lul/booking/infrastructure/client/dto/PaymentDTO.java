package com.lul.booking.infrastructure.client.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDTO {
    private String id;
    private String bookingId;
    private String userId;
    private BigDecimal amount;
    private String status;
    private String paymentMethod;
}