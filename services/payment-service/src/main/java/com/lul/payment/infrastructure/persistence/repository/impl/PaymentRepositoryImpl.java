package com.lul.payment.infrastructure.persistence.repository.impl;

import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import com.lul.payment.domain.payment.aggregate.Payment;
import com.lul.payment.domain.payment.repository.PaymentRepository;
import com.lul.payment.infrastructure.persistence.entity.PaymentEntity;
import com.lul.payment.infrastructure.persistence.mapper.PaymentMapper;
import com.lul.payment.infrastructure.persistence.repository.jpa.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository jpaRepository;
    private final PaymentMapper mapper;

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = mapper.toEntity(payment);
        PaymentEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Payment> findById(String id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Payment> findByBookingId(String bookingId) {
        return jpaRepository.findByBookingId(bookingId)
                .map(mapper::toDomain);
    }
}

