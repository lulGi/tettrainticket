package com.lul.payment.domain.payment.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PaymentCompletedEvent.class, name = "paymentCompleted"),
        @JsonSubTypes.Type(value = PaymentFailedEvent.class, name = "paymentFailed"),
        @JsonSubTypes.Type(value = PaymentRefundedEvent.class, name = "paymentRefunded")
})
public abstract class PaymentEvent {

    private String paymentId;
    private String bookingId;
    private LocalDateTime timestamp;

    protected PaymentEvent(String paymentId, String bookingId) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.timestamp = LocalDateTime.now();
    }

}
