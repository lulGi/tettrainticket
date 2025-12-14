package com.lul.payment.domain.payment.event;

import lombok.Getter;

@Getter
public class PaymentFailedEvent extends PaymentEvent {
    private final String reason;

    public PaymentFailedEvent(String paymentId, String bookingId, String reason) {
        super(paymentId, bookingId);
        this.reason = reason;
    }
}
