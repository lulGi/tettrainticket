package com.lul.booking.domain.booking.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BookingCreatedEvent extends BookingEvent{

    private String userId;
    private String scheduleId;
    private Integer numberOfSeats;
    private BigDecimal totalPrice;

    public BookingCreatedEvent(String bookingId, String userId, String scheduleId,
                               Integer numberOfSeats, BigDecimal totalPrice) {
        super(bookingId, LocalDateTime.now());
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.numberOfSeats = numberOfSeats;
        this.totalPrice = totalPrice;
    }

}
