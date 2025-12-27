package com.lul.notification.infrastructure.kafka.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class PaymentRefundedEvent extends PaymentEvent {
    private BigDecimal refundAmount;
    private String reason;
}
