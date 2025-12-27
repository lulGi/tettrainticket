package com.lul.booking.application.service;

import com.lul.booking.application.dto.mapper.BookingDtoMapper;
import com.lul.booking.application.dto.request.CreateBookingRequest;
import com.lul.booking.application.dto.response.BookingDTO;
import com.lul.booking.domain.booking.aggregate.Booking;
import com.lul.booking.domain.booking.event.BookingCreatedEvent;
import com.lul.booking.domain.booking.repository.BookingRepository;
import com.lul.booking.domain.booking.valueobject.Passenger;
import com.lul.booking.infrastructure.client.PaymentServiceClient;
import com.lul.booking.infrastructure.client.TrainServiceClient;
import com.lul.booking.infrastructure.client.dto.CreatePaymentRequest;
import com.lul.booking.infrastructure.client.dto.ScheduleDTO;
import com.lul.booking.infrastructure.kafka.producer.BookingEventProducer;
import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingApplicationService {

    private final BookingRepository bookingRepository;
    private final TrainServiceClient trainServiceClient;
    private final PaymentServiceClient paymentServiceClient;
    private final BookingEventProducer eventProducer;
    private final BookingDtoMapper dtoMapper;

    /**
     * Create booking (Saga Orchestration)
     * Step 1: Reserve seats (train-service)
     * Step 2: Create booking (PENDING)
     * Step 3: Create payment (payment-service)
     * Step 4: Wait for payment event → confirm or cancel booking
     */
    @Transactional
    public BookingDTO createBooking(CreateBookingRequest request, String userId) {
        log.info("Creating booking for user: {}, schedule: {}", userId, request.getScheduleId());

        // Step 0: Validate schedule exists and get price
        ScheduleDTO schedule = trainServiceClient.getSchedule(request.getScheduleId());
        BigDecimal totalPrice = schedule.getPrice().multiply(BigDecimal.valueOf(request.getNumberOfSeats()));

        // Step 1: Reserve seats (with pessimistic lock in train-service)
        try {
            trainServiceClient.reserveSeats(
                    request.getScheduleId(),
                    new com.lul.booking.infrastructure.client.dto.ReserveSeatRequest(request.getNumberOfSeats())
            );
        } catch (BusinessException e) {
            if (e.getErrorCode() == ErrorCode.SEAT_NOT_AVAILABLE) {
                // 409 Conflict from ErrorDecoder
                throw e;
            }
            // Other errors
            throw new BusinessException(
                    ErrorCode.EXTERNAL_SERVICE_ERROR,
                    "Failed to reserve seats: " + e.getMessage()
            );
        }


        // Step 2: Create booking (PENDING status)
        List<Passenger> passengers = dtoMapper.toDomainList(request.getPassengers());

        Booking booking = Booking.create(
                userId,
                request.getScheduleId(),
                passengers,
                request.getNumberOfSeats(),
                totalPrice
        );

        booking = bookingRepository.save(booking);

        // Publish BookingCreatedEvent
        BookingCreatedEvent createdEvent = new BookingCreatedEvent(
                booking.getId(),
                booking.getUserId(),
                booking.getScheduleId(),
                booking.getNumberOfSeats(),
                booking.getTotalPrice()
        );
        eventProducer.publishEvent(createdEvent);

        // Step 3: Create payment (async - will trigger PaymentCompleted/Failed event)
        try {
            CreatePaymentRequest paymentRequest = new CreatePaymentRequest(
                    booking.getId(),
                    userId,
                    totalPrice,
                    request.getPaymentMethod()
            );
            paymentServiceClient.createPayment(paymentRequest);
        } catch (Exception e) {
            log.error("Payment creation failed, will trigger compensation", e);
            // Compensation will happen via Saga when payment-service publishes PaymentFailedEvent
        }

        log.info("Booking created successfully: {}", booking.getId());
        return dtoMapper.toDTO(booking);
    }

    /**
     * Get booking by ID
     */
    @Transactional(readOnly = true)
    public BookingDTO getBooking(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.BOOKING_NOT_FOUND,
                        "Booking not found: " + bookingId
                ));
        return dtoMapper.toDTO(booking);
    }

    /**
     * Get user bookings
     */
    @Transactional(readOnly = true)
    public List<BookingDTO> getUserBookings(String userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        return dtoMapper.toDTOs(bookings);
    }
}