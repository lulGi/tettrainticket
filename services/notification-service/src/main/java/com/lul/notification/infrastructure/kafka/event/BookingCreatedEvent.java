package com.lul.notification.infrastructure.kafka.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class BookingCreatedEvent extends BookingEvent {
    private String userId;
    private String scheduleId;
    private Integer numberOfSeats;
    private BigDecimal totalPrice;
}
