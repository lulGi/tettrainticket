package com.lul.train.domain.schedule.aggregate;

import com.lul.common.core.domain.AggregateRoot;
import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import com.lul.train.domain.schedule.valueobject.ScheduleStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Schedule extends AggregateRoot<String> {

    private String trainId;
    private String routeId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer availableSeats;  // Simplified: số ghế còn trống
    private BigDecimal price;
    private ScheduleStatus status;

    /**
     * Private constructor - force use of factory method
     */
    private Schedule() {
        super();
    }

    /**
     * Factory method to create new Schedule
     */
    public static Schedule create(
            String trainId,
            String routeId,
            LocalDateTime departureTime,
            LocalDateTime arrivalTime,
            Integer availableSeats,
            BigDecimal price
    ) {
        validateTrainId(trainId);
        validateRouteId(routeId);
        validateTimes(departureTime, arrivalTime);
        validateSeats(availableSeats);
        validatePrice(price);

        Schedule schedule = new Schedule();
        schedule.trainId = trainId;
        schedule.routeId = routeId;
        schedule.departureTime = departureTime;
        schedule.arrivalTime = arrivalTime;
        schedule.availableSeats = availableSeats;
        schedule.price = price;
        schedule.status = ScheduleStatus.SCHEDULED;  // Default status

        return schedule;
    }

    /**
     * Update schedule information
     */
    public void updateInfo(
            LocalDateTime departureTime,
            LocalDateTime arrivalTime,
            Integer availableSeats,
            BigDecimal price
    ) {
        if (departureTime != null && arrivalTime != null) {
            validateTimes(departureTime, arrivalTime);
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
        }
        if (availableSeats != null) {
            validateSeats(availableSeats);
            this.availableSeats = availableSeats;
        }
        if (price != null) {
            validatePrice(price);
            this.price = price;
        }
    }

    /**
     * Reserve seats (for booking - Saga Step 1)
     * This method MUST be called with pessimistic lock to prevent race conditions
     */
    public void reserveSeats(Integer numberOfSeats) {
        if (this.status != ScheduleStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Cannot reserve seats. Schedule is not active");
        }
        if (numberOfSeats == null || numberOfSeats <= 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Number of seats must be greater than 0");
        }
        if (this.availableSeats < numberOfSeats) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Not enough available seats. Available: " + this.availableSeats);
        }

        this.availableSeats -= numberOfSeats;  // BLOCK seats
    }

    /**
     * Release seats (for booking cancellation - Saga Compensation)
     */
    public void releaseSeats(Integer numberOfSeats) {
        if (numberOfSeats == null || numberOfSeats <= 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Number of seats must be greater than 0");
        }

        this.availableSeats += numberOfSeats;  // UNBLOCK seats
    }

    /**
     * Activate schedule (make it available for booking)
     */
    public void activate() {
        if (this.status == ScheduleStatus.CANCELLED) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Cannot activate a cancelled schedule");
        }
        this.status = ScheduleStatus.ACTIVE;
    }

    /**
     * Cancel schedule
     */
    public void cancel() {
        if (this.status == ScheduleStatus.COMPLETED) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Cannot cancel a completed schedule");
        }
        this.status = ScheduleStatus.CANCELLED;
    }

    /**
     * Mark schedule as completed
     */
    public void complete() {
        if (this.status == ScheduleStatus.CANCELLED) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Cannot complete a cancelled schedule");
        }
        this.status = ScheduleStatus.COMPLETED;
    }

    // ============ Validation Methods ============

    private static void validateTrainId(String trainId) {
        if (trainId == null || trainId.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Train ID cannot be empty");
        }
    }

    private static void validateRouteId(String routeId) {
        if (routeId == null || routeId.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Route ID cannot be empty");
        }
    }

    private static void validateTimes(LocalDateTime departureTime, LocalDateTime arrivalTime) {
        if (departureTime == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Departure time cannot be null");
        }
        if (arrivalTime == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Arrival time cannot be null");
        }
        if (!arrivalTime.isAfter(departureTime)) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Arrival time must be after departure time");
        }
    }

    private static void validateSeats(Integer availableSeats) {
        if (availableSeats == null || availableSeats < 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Available seats must be non-negative");
        }
    }

    private static void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Price must be greater than 0");
        }
    }
}
