package com.lul.train.infrastructure.persistence.repository.jpa;

import com.lul.train.infrastructure.persistence.entity.ScheduleEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, String> {

    /**
     * CRITICAL: Pessimistic write lock for concurrency control
     * This generates "SELECT ... FOR UPDATE" SQL
     * Prevents race conditions when reserving seats
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ScheduleEntity s WHERE s.id = :id")
    Optional<ScheduleEntity> findByIdWithLock(@Param("id") String id);

    List<ScheduleEntity> findByRouteId(String routeId);

    List<ScheduleEntity> findByRouteIdAndDepartureTimeBetween(
            String routeId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );
}
