package com.lul.payment.application.service;


import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import com.lul.payment.application.dto.mapper.PaymentDtoMapper;
import com.lul.payment.application.dto.request.CreatePaymentRequest;
import com.lul.payment.application.dto.request.ProcessPaymentRequest;
import com.lul.payment.application.dto.response.PaymentDTO;
import com.lul.payment.domain.payment.aggregate.Payment;
import com.lul.payment.domain.payment.event.PaymentEvent;
import com.lul.payment.domain.payment.event.PaymentRefundedEvent;
import com.lul.payment.domain.payment.repository.PaymentRepository;
import com.lul.payment.infrastructure.kafka.producer.PaymentEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentApplicationService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer eventProducer;
    private final PaymentDtoMapper dtoMapper;

    private final Random random = new Random();

    @Transactional
    public PaymentDTO createPayment(CreatePaymentRequest request) {
        log.info("Creating payment for booking: {}", request.getBookingId());

        Payment payment = Payment.create(
                request.getBookingId(),
                request.getAmount(),
                request.getPaymentMethod()
        );

        Payment savedPayment = paymentRepository.save(payment);

        log.info("Payment created successfully - ID:{}, Status: {}", savedPayment.getId(), savedPayment.getStatus());

        return dtoMapper.toDto(savedPayment);
    }


    @Transactional
    public PaymentDTO processPayment(String paymentId, ProcessPaymentRequest request) {
        log.info("Processing payment: {}", paymentId);

        // Find payment
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND,
                        "Payment not found with ID: " + paymentId));

        // Simulate payment gateway (60% success, 40% fail)
        boolean success = random.nextInt(100) < 60;

        PaymentEvent event;
        if (success) {
            // Payment succeeded
            String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8);
            event = payment.markAsCompleted(transactionId);
            log.info("Payment processed successfully - ID: {}, TransactionId: {}",
                    paymentId, transactionId);
        } else {
            // Payment failed
            String reason = "Insufficient funds / Card declined / Network error";
            event = payment.markAsFailed(reason);
            log.warn("Payment processing failed - ID: {}, Reason: {}", paymentId, reason);
        }


        Payment updatedPayment = paymentRepository.save(payment);

        // Publish event to Kafka AFTER database save
        eventProducer.publishEvent(event);

        return dtoMapper.toDto(updatedPayment);

    }


    @Transactional
    public PaymentDTO refundPayment(String paymentId, String reason) {
        log.info("Refunding payment: {} - Reason: {}", paymentId, reason);

        // Find payment
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND,
                        "Payment not found with ID: " + paymentId));

        // Refund payment
        PaymentRefundedEvent event = payment.refund(reason);

        // Save to database FIRST
        Payment refundedPayment = paymentRepository.save(payment);

        // Publish event to Kafka AFTER database save
        eventProducer.publishEvent(event);

        log.info("Payment refunded successfully - ID: {}", paymentId);

        return dtoMapper.toDto(refundedPayment);
    }

    @Transactional(readOnly = true)
    public PaymentDTO getPaymentById(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND,
                        "Payment not found with ID: " + paymentId));

        return dtoMapper.toDto(payment);
    }

    @Transactional(readOnly = true)
    public PaymentDTO getPaymentByBookingId(String bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND,
                        "Payment not found for booking ID: " + bookingId));

        return dtoMapper.toDto(payment);
    }
}