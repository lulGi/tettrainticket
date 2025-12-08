package com.lul.train.application.dto.response;

import com.lul.train.domain.train.valueobject.TrainType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainDTO {
    private String id;
    private String trainNumber;
    private String trainName;
    private TrainType trainType;
    private Integer totalSeats;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
