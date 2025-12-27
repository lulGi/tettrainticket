package com.lul.notification.infrastructure.kafka.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentFailedEvent extends PaymentEvent {
    private String reason;
}
