package com.lul.booking.domain.booking.repository;

import com.lul.booking.domain.booking.aggregate.Booking;
import com.lul.booking.domain.booking.valueobject.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface BookingRepository {

    Booking save(Booking booking);

    Optional<Booking> findById(String id);

    List<Booking> findByUserId(String userId);

    List<Booking> findByScheduleId(String scheduleId);

    List<Booking> findExpiredBookings(LocalDateTime expiryTime, BookingStatus status);

    void deleteById(String id);
}