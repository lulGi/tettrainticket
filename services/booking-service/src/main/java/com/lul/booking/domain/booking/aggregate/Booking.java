package com.lul.booking.domain.booking.aggregate;

import com.lul.booking.domain.booking.event.BookingCancelledEvent;
import com.lul.booking.domain.booking.event.BookingConfirmedEvent;
import com.lul.booking.domain.booking.valueobject.BookingStatus;
import com.lul.booking.domain.booking.valueobject.Passenger;
import com.lul.common.core.domain.AggregateRoot;
import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Booking extends AggregateRoot<String> {

    private String userId;
    private String scheduleId;
    private List<Passenger> passengers;
    private Integer numberOfSeats;
    private BigDecimal totalPrice;
    private BookingStatus status;
    private String paymentId;
    private String cancellationReason;
    private LocalDateTime expiresAt;

    /**
     * Force use factory method
     */
    private Booking(){
        super();
    }

    public static Booking create(
            String userId,
            String scheduleId,
            List<Passenger> passengers,
            Integer numberOfSeats,
            BigDecimal totalPrice
    ){
        validateUserId(userId);
        validateScheduleId(scheduleId);
        validatePassengers(passengers, numberOfSeats);
        validateNumberOfSeats(numberOfSeats);
        validateTotalPrice(totalPrice);

        Booking booking = new Booking();
        booking.userId = userId;
        booking.scheduleId = scheduleId;
        booking.passengers = passengers;
        booking.numberOfSeats = numberOfSeats;
        booking.totalPrice = totalPrice;
        booking.status = BookingStatus.PENDING;
        booking.expiresAt = LocalDateTime.now().plusMinutes(15);

        return booking;
    }

    private static void validateUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "User ID cannot be empty");
        }
    }

    private static void validateScheduleId(String scheduleId) {
        if (scheduleId == null || scheduleId.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Schedule ID cannot be empty");
        }
    }

    private static void validatePassengers(List<Passenger> passengers, Integer numberOfSeats) {
        if (passengers == null || passengers.isEmpty()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Passengers list cannot be empty");
        }
        if (passengers.size() != numberOfSeats) {
            throw new BusinessException(
                    ErrorCode.VALIDATION_ERROR,
                    "Number of passengers must match number of seats"
            );
        }
    }

    private static void validateNumberOfSeats(Integer numberOfSeats) {
        if (numberOfSeats == null || numberOfSeats <= 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Number of seats must be greater than 0");
        }
        if (numberOfSeats > 10) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Cannot book more than 10 seats at once");
        }
    }

    private static void validateTotalPrice(BigDecimal totalPrice) {
        if (totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Total price must be greater than zero");
        }
    }

    public BookingConfirmedEvent confirm(String paymentId) {
        if (this.status != BookingStatus.PENDING) {
            throw new BusinessException(
                    ErrorCode.INVALID_STATE,
                    "Only PENDING bookings can be confirmed"
            );
        }

        this.status = BookingStatus.CONFIRMED;
        this.paymentId = paymentId;

        return new BookingConfirmedEvent(this.getId(), this.userId, paymentId);
    }

    public BookingCancelledEvent cancel(String reason) {
        if (this.status == BookingStatus.CONFIRMED) {
            throw new BusinessException(
                    ErrorCode.INVALID_STATE,
                    "Cannot cancel confirmed booking. Please request refund instead."
            );
        }

        if (this.status == BookingStatus.CANCELLED || this.status == BookingStatus.EXPIRED) {
            throw new BusinessException(
                    ErrorCode.INVALID_STATE,
                    "Booking is already " + this.status
            );
        }

        this.status = BookingStatus.CANCELLED;
        this.cancellationReason = reason;

        return new BookingCancelledEvent(this.getId(), this.userId, reason);
    }

    public void markAsExpired() {
        if (this.status != BookingStatus.PENDING) {
            throw new BusinessException(
                    ErrorCode.INVALID_STATE,
                    "Only PENDING bookings can be expired"
            );
        }

        this.status = BookingStatus.EXPIRED;
        this.cancellationReason = "Booking expired after 15 minutes";
    }

    public boolean isPending() {
        return this.status == BookingStatus.PENDING;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

}
