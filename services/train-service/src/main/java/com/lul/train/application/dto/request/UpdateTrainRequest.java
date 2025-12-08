package com.lul.train.application.dto.request;

import com.lul.train.domain.train.valueobject.TrainType;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateTrainRequest {

    private String trainName;
    private TrainType trainType;

    @Positive(message = "Total seats must be positive")
    private Integer totalSeats;
}
