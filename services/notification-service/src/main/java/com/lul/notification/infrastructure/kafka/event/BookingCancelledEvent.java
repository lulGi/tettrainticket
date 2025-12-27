package com.lul.notification.infrastructure.kafka.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookingCancelledEvent extends BookingEvent {
    private String userId;
    private String reason;
}
