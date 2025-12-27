package com.lul.payment.domain.payment.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentFailedEvent extends PaymentEvent {
    private String reason;

    public PaymentFailedEvent(String paymentId, String bookingId, String reason) {
        super(paymentId, bookingId);
        this.reason = reason;
    }
}
