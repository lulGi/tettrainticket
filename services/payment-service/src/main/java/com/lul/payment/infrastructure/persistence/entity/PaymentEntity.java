package com.lul.payment.infrastructure.persistence.entity;

import com.lul.common.core.infrastructure.persistence.BaseJpaEntity;
import com.lul.payment.domain.payment.valueobject.PaymentMethod;
import com.lul.payment.domain.payment.valueobject.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PaymentEntity extends BaseJpaEntity {

    @Column(name = "booking_id", nullable = false)
    private String bookingId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "failure_reason")
    private String failureReason;

}
