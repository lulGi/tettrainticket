package com.lul.booking.infrastructure.scheduler;

import com.lul.booking.domain.booking.aggregate.Booking;
import com.lul.booking.domain.booking.event.BookingCancelledEvent;
import com.lul.booking.domain.booking.repository.BookingRepository;
import com.lul.booking.domain.booking.valueobject.BookingStatus;
import com.lul.booking.infrastructure.client.TrainServiceClient;
import com.lul.booking.infrastructure.kafka.producer.BookingEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Scheduled task to auto-expire pending bookings after 15 minutes
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BookingExpiryScheduler {

    private final BookingRepository bookingRepository;
    private final TrainServiceClient trainServiceClient;
    private final BookingEventProducer eventProducer;

    @Scheduled(cron = "${booking.scheduler.cron}")
    @Transactional
    public void expireBookings() {
        log.info("Running booking expiry scheduler...");

        LocalDateTime now = LocalDateTime.now();
        List<Booking> expiredBookings = bookingRepository.findExpiredBookings(now, BookingStatus.PENDING);

        log.info("Found {} expired bookings", expiredBookings.size());

        for (Booking booking : expiredBookings) {
            try {
                // Mark as expired
                booking.markAsExpired();
                bookingRepository.save(booking);

                // Release seats
                trainServiceClient.releaseSeats(booking.getScheduleId(), 
                        new com.lul.booking.infrastructure.client.dto.ReserveSeatRequest(booking.getNumberOfSeats()));

                // Publish event
                BookingCancelledEvent event = new BookingCancelledEvent(
                        booking.getId(),
                        booking.getUserId(),
                        "Booking expired after 15 minutes"
                );
                eventProducer.publishEvent(event);

                log.info("Booking expired and seats released: {}", booking.getId());
            } catch (Exception e) {
                log.error("Error expiring booking: {}", booking.getId(), e);
            }
        }
    }
}