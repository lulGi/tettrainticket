package com.lul.payment.domain.payment.event;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PaymentRefundedEvent extends PaymentEvent {
    private final BigDecimal refundAmount;
    private final String reason;

    public PaymentRefundedEvent(String paymentId, String bookingId, BigDecimal refundAmount, String reason) {
        super(paymentId, bookingId);
        this.refundAmount = refundAmount;
        this.reason = reason;
    }
}
