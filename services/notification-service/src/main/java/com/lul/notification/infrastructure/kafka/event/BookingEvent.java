package com.lul.notification.infrastructure.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BookingEvent {
    protected String bookingId;
    protected LocalDateTime occurredAt;
}
