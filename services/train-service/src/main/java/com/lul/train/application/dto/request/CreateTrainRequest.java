package com.lul.train.application.dto.request;

import com.lul.train.domain.train.valueobject.TrainType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateTrainRequest {

    @NotNull(message = "Train number is required")
    private String trainNumber;

    @NotNull(message = "Train name is required")
    private String trainName;

    @NotNull(message = "Train type is required")
    private TrainType trainType;

    @NotNull(message = "Total seats is required")
    @Positive(message = "Total seats must be positive")
    private Integer totalSeats;
}
