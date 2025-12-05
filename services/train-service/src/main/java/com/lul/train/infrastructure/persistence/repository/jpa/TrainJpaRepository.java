package com.lul.train.infrastructure.persistence.repository.jpa;

import com.lul.train.infrastructure.persistence.entity.TrainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainJpaRepository extends JpaRepository<TrainEntity, String> {
    Optional<TrainEntity> findByTrainNumber(String trainNumber);
}
