package com.lul.train.infrastructure.persistence.entity;

import com.lul.common.core.infrastructure.persistence.BaseJpaEntity;
import com.lul.train.domain.schedule.valueobject.ScheduleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedules")
@Getter
@Setter
public class ScheduleEntity extends BaseJpaEntity {

    @Column(name = "train_id", nullable = false, length = 36)
    private String trainId;

    @Column(name = "route_id", nullable = false, length = 36)
    private String routeId;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ScheduleStatus status;
}
