package com.lul.notification.infrastructure.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class PaymentEvent {
    protected String paymentId;
    protected String bookingId;
    protected LocalDateTime timestamp;
}
