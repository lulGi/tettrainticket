package com.lul.payment.domain.payment.event;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PaymentCompletedEvent extends PaymentEvent{

    private final BigDecimal amount;
    private final String transactionId;

    public PaymentCompletedEvent(String paymentId, String bookingId, BigDecimal amount, String transactionId) {
        super(paymentId,bookingId);
        this.amount=amount;
        this.transactionId= transactionId;
    }

}
