package com.lul.booking.domain.booking.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BookingConfirmedEvent extends BookingEvent {
    private String userId;
    private String paymentId;

    public BookingConfirmedEvent(String bookingId, String userId, String paymentId) {
        super(bookingId, LocalDateTime.now());
        this.userId = userId;
        this.paymentId = paymentId;
    }
}