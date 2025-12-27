package com.lul.notification.infrastructure.kafka.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class PaymentCompletedEvent extends PaymentEvent {
    private BigDecimal amount;
    private String transactionId;
}
