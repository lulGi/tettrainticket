package com.lul.payment.domain.payment.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PaymentCompletedEvent extends PaymentEvent {

    private BigDecimal amount;
    private String transactionId;

    public PaymentCompletedEvent(String paymentId, String bookingId, BigDecimal amount, String transactionId) {
        super(paymentId, bookingId);
        this.amount = amount;
        this.transactionId = transactionId;
    }
}
