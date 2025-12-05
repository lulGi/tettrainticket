package com.lul.train.domain.train.aggregate;

import com.lul.common.core.domain.AggregateRoot;
import com.lul.train.domain.train.valueobject.TrainType;
import lombok.Getter;

@Getter
public class Train extends AggregateRoot<String> {

    private String trainNumber;      // Số hiệu tàu (VD: SE1, SE2)
    private String trainName;        // Tên tàu
    private TrainType trainType;     // Loại tàu
    private Integer totalSeats;      // Tổng số ghế


    /**
     * Private constructor - force use of factory method
     */
    private Train() {
    }


    /**
     * Factory method to create new Train
     */
    public static Train create(
            String trainNumber,
            String trainName,
            TrainType trainType,
            Integer totalSeats
    ) {
        if (trainNumber == null || trainNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Train number cannot be null or empty");
        }
        if (trainName == null || trainName.trim().isEmpty()) {
            throw new IllegalArgumentException("Train name cannot be null or empty");
        }
        if (trainType == null) {
            throw new IllegalArgumentException("Train type cannot be null");
        }
        if (totalSeats == null || totalSeats <= 0) {
            throw new IllegalArgumentException("Total seats must be greater than 0");
        }

        Train train = new Train();
        train.trainNumber = trainNumber;
        train.trainName = trainName;
        train.trainType = trainType;
        train.totalSeats = totalSeats;

        return train;
    }

    /**
     * Update train information
     */
    public void updateInfo(String trainName, TrainType trainType, Integer totalSeats) {
        if (trainName != null && !trainName.trim().isEmpty()) {
            this.trainName = trainName;
        }
        if (trainType != null) {
            this.trainType = trainType;
        }
        if (totalSeats != null && totalSeats > 0) {
            this.totalSeats = totalSeats;
        }
    }

}
