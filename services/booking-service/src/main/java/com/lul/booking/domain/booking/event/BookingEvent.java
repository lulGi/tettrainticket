package com.lul.booking.domain.booking.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BookingEvent {


    private String bookingId;
    private LocalDateTime occurredAt;

}
