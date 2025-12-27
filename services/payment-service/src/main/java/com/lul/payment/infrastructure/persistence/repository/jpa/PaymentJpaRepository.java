package com.lul.payment.infrastructure.persistence.repository.jpa;

import com.lul.payment.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, String> {
    @Query("SELECT p FROM PaymentEntity p WHERE p.bookingId = :bookingId ORDER BY p.createdAt DESC LIMIT 1")
    Optional<PaymentEntity> findByBookingId(@Param("bookingId") String bookingId);
}
