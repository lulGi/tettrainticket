package com.lul.booking.infrastructure.persistence.mapper;


import com.lul.booking.domain.booking.aggregate.Booking;
import com.lul.booking.domain.booking.valueobject.Passenger;
import com.lul.booking.infrastructure.persistence.entity.BookingEntity;
import com.lul.booking.infrastructure.persistence.entity.PassengerEmbeddable;
import com.lul.common.core.domain.BaseEntity;
import org.mapstruct.Mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    default BookingEntity toEntity(Booking domain) {
        if (domain == null) {
            return null;
        }

        BookingEntity entity = new BookingEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setScheduleId(domain.getScheduleId());
        entity.setPassengers(toPassengerEmbeddables(domain.getPassengers()));
        entity.setNumberOfSeats(domain.getNumberOfSeats());
        entity.setTotalPrice(domain.getTotalPrice());
        entity.setStatus(domain.getStatus());
        entity.setPaymentId(domain.getPaymentId());
        entity.setCancellationReason(domain.getCancellationReason());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setVersion(domain.getVersion());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }

    default Booking toDomain(BookingEntity entity) {
        if (entity == null) {
            return null;
        }

        Booking booking = Booking.create(
                entity.getUserId(),
                entity.getScheduleId(),
                toPassengers(entity.getPassengers()),
                entity.getNumberOfSeats(),
                entity.getTotalPrice()
        );

        try {
            Field statusField = Booking.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(booking, entity.getStatus());
        } catch (Exception e) {
            throw new RuntimeException("Failed to set status field", e);
        }


        try {
            Field paymentIdField = Booking.class.getDeclaredField("paymentId");
            paymentIdField.setAccessible(true);
            paymentIdField.set(booking, entity.getPaymentId());

            Field cancellationReasonField = Booking.class.getDeclaredField("cancellationReason");
            cancellationReasonField.setAccessible(true);
            cancellationReasonField.set(booking, entity.getCancellationReason());

            Field expiresAtField = Booking.class.getDeclaredField("expiresAt");
            expiresAtField.setAccessible(true);
            expiresAtField.set(booking, entity.getExpiresAt());
        } catch (Exception e) {
            throw new RuntimeException("Failed to set domain fields", e);
        }

        setBaseEntityFields(booking, entity);

        return booking;


    }

    default void setBaseEntityFields(Booking domain, BookingEntity entity) {
        try {
            Field idField = BaseEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(domain, entity.getId());

            Field versionField = BaseEntity.class.getDeclaredField("version");
            versionField.setAccessible(true);
            versionField.set(domain, entity.getVersion());

            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(domain, entity.getCreatedAt());

            Field updatedAtField = BaseEntity.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(domain, entity.getUpdatedAt());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set BaseEntity fields via reflection", e);
        }
    }

    default List<PassengerEmbeddable> toPassengerEmbeddables(List<Passenger> passengers) {
        if (passengers == null) {
            return null;
        }
        return passengers.stream()
                .map(p -> new PassengerEmbeddable(
                        p.getFullName(),
                        p.getIdentityNumber(),
                        p.getPhoneNumber()
                ))
                .collect(Collectors.toList());
    }


    default List<Passenger> toPassengers(List<PassengerEmbeddable> embeddables) {
        if (embeddables == null) {
            return null;
        }
        return embeddables.stream()
                .map(e -> Passenger.create(
                        e.getFullName(),
                        e.getIdentityNumber(),
                        e.getPhoneNumber()
                ))
                .collect(Collectors.toList());
    }

}
