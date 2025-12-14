package com.lul.payment.infrastructure.persistence.mapper;

import com.lul.payment.domain.payment.aggregate.Payment;
import com.lul.payment.infrastructure.persistence.entity.PaymentEntity;
import org.mapstruct.Mapper;

import java.lang.reflect.Field;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    /**
     * Domain → Entity
     */
    PaymentEntity toEntity(Payment payment);

    /**
     * Entity → Domain
     */
    default Payment toDomain(PaymentEntity entity) {
        if (entity == null) return null;

        // Dùng reconstitute() thay vì create()
        Payment payment = Payment.reconstitute(
                entity.getBookingId(),
                entity.getAmount(),
                entity.getPaymentMethod(),
                entity.getStatus(),
                entity.getTransactionId(),
                entity.getFailureReason()
        );

        // Chỉ còn set BaseEntity fields
        setBaseEntityFields(payment, entity);

        return payment;
    }

    /**
     * Set BaseEntity fields (id, version, timestamps)
     */
    default void setBaseEntityFields(Payment payment, PaymentEntity entity) {
        try {
            Class<?> baseClass = payment.getClass().getSuperclass().getSuperclass();

            String[] fields = {"id", "version", "createdAt", "updatedAt"};
            Object[] values = {
                    entity.getId(),
                    entity.getVersion(),
                    entity.getCreatedAt(),
                    entity.getUpdatedAt()
            };

            for (int i = 0; i < fields.length; i++) {
                Field field = baseClass.getDeclaredField(fields[i]);
                field.setAccessible(true);
                field.set(payment, values[i]);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to set BaseEntity fields", e);
        }
    }

}