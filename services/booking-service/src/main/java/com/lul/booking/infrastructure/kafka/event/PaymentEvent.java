package com.lul.booking.infrastructure.kafka.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Payment events consumed from payment-service
 * NOTE: Matches the same JsonTypeInfo structure from payment-service
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PaymentCompletedEvent.class, name = "paymentCompleted"),
    @JsonSubTypes.Type(value = PaymentFailedEvent.class, name = "paymentFailed")
})
public abstract class PaymentEvent {
    protected String paymentId;
    protected String bookingId;
    protected LocalDateTime timestamp;  // Match payment-service field name
}