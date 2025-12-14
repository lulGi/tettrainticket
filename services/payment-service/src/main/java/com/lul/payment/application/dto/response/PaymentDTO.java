package com.lul.payment.application.dto.response;

import com.lul.payment.domain.payment.valueobject.PaymentMethod;
import com.lul.payment.domain.payment.valueobject.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private String id;
    private String bookingId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String transactionId;
    private String failureReason;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
