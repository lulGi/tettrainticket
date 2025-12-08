package com.lul.train.application.dto.response;

import com.lul.train.domain.schedule.valueobject.ScheduleStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ScheduleDTO {
    private String id;
    private String trainId;
    private String routeId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer availableSeats;
    private BigDecimal price;
    private ScheduleStatus status;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
