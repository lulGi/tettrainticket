package com.lul.booking.application.service;

import com.lul.booking.domain.booking.aggregate.Booking;
import com.lul.booking.domain.booking.event.BookingCancelledEvent;
import com.lul.booking.domain.booking.event.BookingConfirmedEvent;
import com.lul.booking.domain.booking.repository.BookingRepository;
import com.lul.booking.infrastructure.client.TrainServiceClient;
import com.lul.booking.infrastructure.client.dto.ReserveSeatRequest;
import com.lul.booking.infrastructure.kafka.producer.BookingEventProducer;
import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Saga Orchestration Service
 * Handles distributed transaction coordination between booking, train, and payment services
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookingSagaService {

    private final BookingRepository bookingRepository;
    private final TrainServiceClient trainServiceClient;
    private final BookingEventProducer eventProducer;

    /**
     * Handle PaymentCompleted event from payment-service (Saga Step 4)
     */
    @Transactional
    public void handlePaymentCompleted(String bookingId, String paymentId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.BOOKING_NOT_FOUND,
                        "Booking not found: " + bookingId
                ));

        // Idempotency check
        if (booking.getStatus() == com.lul.booking.domain.booking.valueobject.BookingStatus.CONFIRMED) {
            log.warn("Booking already confirmed, skipping duplicate event: {}", bookingId);
            return;
        }

        // Confirm booking
        BookingConfirmedEvent event = booking.confirm(paymentId);
        bookingRepository.save(booking);

        // Publish event
        eventProducer.publishEvent(event);

        log.info("Booking confirmed successfully: {}", bookingId);
    }

    /**
     * Handle PaymentFailed event from payment-service (Saga Compensation)
     */
    @Transactional
    public void handlePaymentFailed(String bookingId, String reason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.BOOKING_NOT_FOUND,
                        "Booking not found: " + bookingId
                ));

        // Idempotency check
        if (booking.getStatus() == com.lul.booking.domain.booking.valueobject.BookingStatus.CANCELLED) {
            log.warn("Booking already cancelled, skipping duplicate event: {}", bookingId);
            return;
        }

        // Cancel booking
        BookingCancelledEvent event = booking.cancel("Payment failed: " + reason);
        bookingRepository.save(booking);

        // Compensate: Release seats
        try {
            trainServiceClient.releaseSeats(booking.getScheduleId(), 
                    new ReserveSeatRequest(booking.getNumberOfSeats()));
            log.info("Successfully released {} seats for schedule {}",
                    booking.getNumberOfSeats(), booking.getScheduleId());
        } catch (Exception e) {
            // DO NOT throw - compensation must be idempotent
            log.error("Failed to release seats during compensation for booking: {}", bookingId, e);
            // In production: send alert, queue for manual retry, or publish compensation failed event
        }


        // Publish event
        eventProducer.publishEvent(event);

        log.info("Booking cancelled due to payment failure: {}", bookingId);
    }

    /**
     * Cancel booking manually (user-initiated)
     */
    @Transactional
    public void cancelBooking(String bookingId, String reason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.BOOKING_NOT_FOUND,
                        "Booking not found: " + bookingId
                ));

        // Cancel booking
        BookingCancelledEvent event = booking.cancel(reason);
        bookingRepository.save(booking);

        // Compensate: Release seats
        try {
            trainServiceClient.releaseSeats(booking.getScheduleId(), 
                    new ReserveSeatRequest(booking.getNumberOfSeats()));
            log.info("Successfully released {} seats for schedule {}",
                    booking.getNumberOfSeats(), booking.getScheduleId());
        } catch (Exception e) {
            // DO NOT throw - compensation must be idempotent
            log.error("Failed to release seats during compensation for booking: {}", bookingId, e);
            // In production: send alert, queue for manual retry, or publish compensation failed event
        }


        // Publish event
        eventProducer.publishEvent(event);

        log.info("Booking cancelled by user: {}", bookingId);
    }
}