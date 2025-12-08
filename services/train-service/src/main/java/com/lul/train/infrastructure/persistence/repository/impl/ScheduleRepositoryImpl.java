package com.lul.train.infrastructure.persistence.repository.impl;

import com.lul.train.domain.schedule.aggregate.Schedule;
import com.lul.train.domain.schedule.repository.ScheduleRepository;
import com.lul.train.infrastructure.persistence.entity.ScheduleEntity;
import com.lul.train.infrastructure.persistence.mapper.ScheduleMapper;
import com.lul.train.infrastructure.persistence.repository.jpa.ScheduleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final ScheduleJpaRepository jpaRepository;
    private final ScheduleMapper mapper;

    @Override
    public Schedule save(Schedule schedule) {
        ScheduleEntity entity = mapper.toEntity(schedule);
        ScheduleEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Schedule> findById(String id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Schedule> findByIdWithLock(String id) {
        return jpaRepository.findByIdWithLock(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Schedule> findByRouteIdAndDepartureTimeBetween(
            String routeId,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        return jpaRepository.findByRouteIdAndDepartureTimeBetween(routeId, startTime, endTime)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
