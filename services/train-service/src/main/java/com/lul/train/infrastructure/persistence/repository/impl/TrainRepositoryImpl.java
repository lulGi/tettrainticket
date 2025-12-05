package com.lul.train.infrastructure.persistence.repository.impl;

import com.lul.train.domain.train.aggregate.Train;
import com.lul.train.domain.train.repository.TrainRepository;
import com.lul.train.infrastructure.persistence.entity.TrainEntity;
import com.lul.train.infrastructure.persistence.mapper.TrainMapper;
import com.lul.train.infrastructure.persistence.repository.jpa.TrainJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TrainRepositoryImpl implements TrainRepository {

    private final TrainJpaRepository jpaRepository;
    private final TrainMapper trainMapper;

    @Override
    public Train save(Train train) {

        TrainEntity entity = trainMapper.toEntity(train);
        TrainEntity saved = jpaRepository.save(entity);
        return trainMapper.toDomain(saved);

    }

    @Override
    public Optional<Train> findById(String id) {

        return jpaRepository.findById(id).map(trainMapper::toDomain);
    }

    @Override
    public Optional<Train> findByTrainNumber(String trainNumber) {
        return jpaRepository.findByTrainNumber(trainNumber).map(trainMapper::toDomain);
    }

    @Override
    public List<Train> findAll() {
        return jpaRepository.findAll().stream()
                .map(trainMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void delete(Train train) {
        jpaRepository.deleteById(train.getId());
    }
}
