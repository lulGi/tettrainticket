package com.lul.booking.infrastructure.persistence.repository.impl;

import com.lul.booking.domain.booking.aggregate.Booking;
import com.lul.booking.domain.booking.repository.BookingRepository;
import com.lul.booking.domain.booking.valueobject.BookingStatus;
import com.lul.booking.infrastructure.persistence.entity.BookingEntity;
import com.lul.booking.infrastructure.persistence.mapper.BookingMapper;
import com.lul.booking.infrastructure.persistence.repository.jpa.BookingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {

    private final BookingJpaRepository jpaRepository;
    private final BookingMapper mapper;

    @Override
    public Booking save(Booking booking) {
        BookingEntity entity = mapper.toEntity(booking);
        BookingEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Booking> findById(String id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Booking> findByUserId(String userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByScheduleId(String scheduleId) {
        return jpaRepository.findByScheduleId(scheduleId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> findExpiredBookings(LocalDateTime expiryTime, BookingStatus status) {
        return jpaRepository.findExpiredBookings(expiryTime, status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}