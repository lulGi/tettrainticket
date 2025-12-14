package com.lul.payment.domain.payment.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
