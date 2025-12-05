package com.lul.train.domain.train.repository;

import com.lul.train.domain.train.aggregate.Train;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository interface for Train aggregate
 * Implementation will be in infrastructure layer
 */
public interface TrainRepository {

    Train save(Train train);
    Optional<Train> findById(String id);
    Optional<Train> findByTrainNumber(String trainNumber);
    List<Train> findAll();
    void delete(Train train);

}
