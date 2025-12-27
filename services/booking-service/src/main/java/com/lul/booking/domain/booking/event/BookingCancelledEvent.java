package com.lul.booking.domain.booking.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BookingCancelledEvent extends BookingEvent {
    private String userId;
    private String reason;

    public BookingCancelledEvent(String bookingId, String userId, String reason) {
        super(bookingId, LocalDateTime.now());
        this.userId = userId;
        this.reason = reason;
    }
}