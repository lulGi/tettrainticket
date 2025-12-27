package com.lul.booking.infrastructure.persistence.repository.jpa;

import com.lul.booking.domain.booking.valueobject.BookingStatus;
import com.lul.booking.infrastructure.persistence.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingJpaRepository extends JpaRepository<BookingEntity, String> {

    List<BookingEntity> findByUserId(String userId);

    List<BookingEntity> findByScheduleId(String scheduleId);

    @Query("SELECT b FROM BookingEntity b WHERE b.expiresAt < :expiryTime AND b.status = :status")
    List<BookingEntity> findExpiredBookings(
            @Param("expiryTime") LocalDateTime expiryTime,
            @Param("status") BookingStatus status
    );
}