package com.lul.booking.infrastructure.kafka.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PaymentFailedEvent extends PaymentEvent {
    private String reason;

    public PaymentFailedEvent(String paymentId, String bookingId, String reason) {
        super(paymentId, bookingId, LocalDateTime.now());
        this.reason = reason;
    }
}