package com.lul.payment.presentation.controller;

import com.lul.common.core.response.ApiResponse;
import com.lul.payment.application.dto.request.CreatePaymentRequest;
import com.lul.payment.application.dto.request.ProcessPaymentRequest;
import com.lul.payment.application.dto.response.PaymentDTO;
import com.lul.payment.application.service.PaymentApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentApplicationService paymentService;

    /**
     * Create new payment
     * POST /api/payments
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentDTO>> createPayment(
            @Valid @RequestBody CreatePaymentRequest request) {

        PaymentDTO payment = paymentService.createPayment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(payment, "Payment created successfully"));
    }

    /**
     * Process payment (simulate payment gateway)
     * POST /api/payments/{id}/process
     */
    @PostMapping("/{id}/process")
    public ResponseEntity<ApiResponse<PaymentDTO>> processPayment(
            @PathVariable String id,
            @RequestBody ProcessPaymentRequest request) {

        PaymentDTO payment = paymentService.processPayment(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(payment, "Payment processed successfully"));
    }

    /**
     * Refund payment (Saga compensation)
     * POST /api/payments/{id}/refund
     */
    @PostMapping("/{id}/refund")
    public ResponseEntity<ApiResponse<PaymentDTO>> refundPayment(
            @PathVariable String id,
            @RequestParam String reason) {

        PaymentDTO payment = paymentService.refundPayment(id, reason);

        return ResponseEntity.ok(
                ApiResponse.success(payment, "Payment refunded successfully"));
    }

    /**
     * Get payment by ID
     * GET /api/payments/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentDTO>> getPaymentById(
            @PathVariable String id) {

        PaymentDTO payment = paymentService.getPaymentById(id);

        return ResponseEntity.ok(
                ApiResponse.success(payment, "Payment retrieved successfully"));
    }

    /**
     * Get payment by booking ID
     * GET /api/payments/booking/{bookingId}
     */
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<PaymentDTO>> getPaymentByBookingId(
            @PathVariable String bookingId) {

        PaymentDTO payment = paymentService.getPaymentByBookingId(bookingId);

        return ResponseEntity.ok(
                ApiResponse.success(payment, "Payment retrieved successfully"));
    }
}
