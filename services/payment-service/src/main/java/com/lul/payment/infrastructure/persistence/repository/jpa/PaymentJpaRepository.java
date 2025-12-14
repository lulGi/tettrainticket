package com.lul.payment.infrastructure.persistence.repository.jpa;

import com.lul.payment.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, String> {
    Optional<PaymentEntity> findByBookingId(String bookingId);
}
