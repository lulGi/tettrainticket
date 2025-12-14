package com.lul.payment.domain.payment.repository;

import com.lul.payment.domain.payment.aggregate.Payment;

import java.util.Optional;

/**
 * Domain repository interface
 */
public interface PaymentRepository {
    Payment save(Payment payment);

    Optional<Payment> findById(String id);

    Optional<Payment> findByBookingId(String bookingId);
}
