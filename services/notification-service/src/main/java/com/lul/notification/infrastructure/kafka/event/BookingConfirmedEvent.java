package com.lul.notification.infrastructure.kafka.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookingConfirmedEvent extends BookingEvent {
    private String userId;
    private String paymentId;
}
