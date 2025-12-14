package com.lul.payment.domain.payment.aggregate;

import com.lul.common.core.domain.AggregateRoot;
import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import com.lul.payment.domain.payment.event.PaymentCompletedEvent;
import com.lul.payment.domain.payment.event.PaymentFailedEvent;
import com.lul.payment.domain.payment.event.PaymentRefundedEvent;
import com.lul.payment.domain.payment.valueobject.PaymentMethod;
import com.lul.payment.domain.payment.valueobject.PaymentStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
public class Payment extends AggregateRoot<String> {

    private String bookingId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String transactionId;
    private String failureReason;

    private Payment(){
        super();
    }

    public static Payment create(
            String bookingId,
            BigDecimal amount,
            PaymentMethod paymentMethod
    ){
        validateBookingId(bookingId);
        validateAmount(amount);
        validatePaymentMethod(paymentMethod);

        Payment payment = new Payment();
        payment.bookingId = bookingId;
        payment.amount = amount;
        payment.paymentMethod = paymentMethod;
        payment.status = PaymentStatus.PENDING;  // Default status

        return payment;
    }

    public static Payment reconstitute(
            String bookingId,
            BigDecimal amount,
            PaymentMethod paymentMethod,
            PaymentStatus status,
            String transactionId,
            String failureReason
    ) {

        Objects.requireNonNull(bookingId, "bookingId is required");
        Objects.requireNonNull(amount, "amount is required");
        Objects.requireNonNull(paymentMethod, "paymentMethod is required");
        Objects.requireNonNull(status, "status is required");

        Payment payment = new Payment();
        payment.bookingId = bookingId;
        payment.amount = amount;
        payment.paymentMethod = paymentMethod;
        payment.status = status;
        payment.transactionId = transactionId;
        payment.failureReason = failureReason;
        return payment;
    }


    public PaymentCompletedEvent markAsCompleted(String transactionId) {
        if (this.status != PaymentStatus.PENDING) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Cannot complete payment. Current status: " + this.status);
        }
        if (transactionId == null || transactionId.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Transaction ID is required");
        }

        this.status = PaymentStatus.COMPLETED;
        this.transactionId = transactionId;

        // Return event (application service will publish)
        return new PaymentCompletedEvent(this.getId(), this.bookingId, this.amount, transactionId);
    }

    public PaymentFailedEvent markAsFailed(String reason) {
        if (this.status != PaymentStatus.PENDING) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Cannot fail payment. Current status: " + this.status);
        }
        if (reason == null || reason.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Failure reason is required");
        }

        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;

        // Return event (application service will publish)
        return new PaymentFailedEvent(this.getId(), this.bookingId, reason);
    }

    public PaymentRefundedEvent refund(String reason) {
        if (this.status != PaymentStatus.COMPLETED) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Cannot refund payment. Only COMPLETED payments can be refunded. Current status: " + this.status);
        }
        if (reason == null || reason.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Refund reason is required");
        }

        this.status = PaymentStatus.REFUNDED;

        // Return event (application service will publish)
        return new PaymentRefundedEvent(this.getId(), this.bookingId, this.amount, reason);
    }


    private static void validateBookingId(String bookingId) {
        if (bookingId == null || bookingId.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Booking ID cannot be empty");
        }
    }

    private static void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Payment amount must be greater than 0");
        }
    }

    private static void validatePaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Payment method is required");
        }
    }

}
