package com.lul.booking.infrastructure.kafka.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PaymentCompletedEvent extends PaymentEvent {
    private BigDecimal amount;
    private String transactionId;

    public PaymentCompletedEvent(String paymentId, String bookingId, BigDecimal amount, String transactionId) {
        super(paymentId, bookingId, LocalDateTime.now());
        this.amount = amount;
        this.transactionId = transactionId;
    }
}