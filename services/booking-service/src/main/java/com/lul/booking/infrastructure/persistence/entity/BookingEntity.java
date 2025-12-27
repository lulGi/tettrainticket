package com.lul.booking.infrastructure.persistence.entity;

import com.lul.booking.domain.booking.valueobject.BookingStatus;
import com.lul.common.core.infrastructure.persistence.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
public class BookingEntity extends BaseJpaEntity {

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "schedule_id", nullable = false, length = 36)
    private String scheduleId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "booking_passengers", joinColumns = @JoinColumn(name = "booking_id"))
    private List<PassengerEmbeddable> passengers = new ArrayList<>();

    @Column(name = "number_of_seats", nullable = false)
    private Integer numberOfSeats;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private BookingStatus status;

    @Column(name = "payment_id", length = 36)
    private String paymentId;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}