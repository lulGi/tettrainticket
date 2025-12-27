package com.lul.train.domain.schedule.repository;

import com.lul.train.domain.schedule.aggregate.Schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Domain repository interface for Schedule
 * No Spring dependencies here - pure domain
 */
public interface ScheduleRepository {

    Schedule save(Schedule schedule);

    Optional<Schedule> findById(String id);

    List<Schedule> findAll();

    /**
     * Find schedule by ID with pessimistic lock
     * CRITICAL: This method MUST acquire database-level lock (SELECT FOR UPDATE)
     * Used for reserve/release operations to prevent race conditions
     */
    Optional<Schedule> findByIdWithLock(String id);

    List<Schedule> findByRouteId(String routeId);

    List<Schedule> findByRouteIdAndDepartureTimeBetween(
            String routeId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    void deleteById(String id);
}
