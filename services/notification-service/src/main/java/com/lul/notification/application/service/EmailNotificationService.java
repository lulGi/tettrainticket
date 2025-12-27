package com.lul.notification.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailNotificationService {

    /**
     * Mock: Send booking created email
     */
    public void sendBookingCreatedEmail(String userId, String bookingId, String scheduleId, int seats, String amount) {
        log.info("📧 [MOCK EMAIL] Sending booking created email:");
        log.info("   To User: {}", userId);
        log.info("   Booking ID: {}", bookingId);
        log.info("   Schedule: {}", scheduleId);
        log.info("   Seats: {}", seats);
        log.info("   Total: {}", amount);
        log.info("   Subject: Booking Created - Pending Payment");
        log.info("   Body: Your booking has been created. Please complete payment within 15 minutes.");
    }

    /**
     * Mock: Send booking confirmed email
     */
    public void sendBookingConfirmedEmail(String userId, String bookingId, String paymentId) {
        log.info("📧 [MOCK EMAIL] Sending booking confirmed email:");
        log.info("   To User: {}", userId);
        log.info("   Booking ID: {}", bookingId);
        log.info("   Payment ID: {}", paymentId);
        log.info("   Subject: Booking Confirmed - Ticket Ready");
        log.info("   Body: Your booking has been confirmed. Your ticket is ready for download.");
    }

    /**
     * Mock: Send booking cancelled email
     */
    public void sendBookingCancelledEmail(String userId, String bookingId, String reason) {
        log.info("📧 [MOCK EMAIL] Sending booking cancelled email:");
        log.info("   To User: {}", userId);
        log.info("   Booking ID: {}", bookingId);
        log.info("   Reason: {}", reason);
        log.info("   Subject: Booking Cancelled");
        log.info("   Body: Your booking has been cancelled. Reason: {}", reason);
    }

    /**
     * Mock: Send payment completed email
     */
    public void sendPaymentCompletedEmail(String bookingId, String paymentId, String amount, String transactionId) {
        log.info("📧 [MOCK EMAIL] Sending payment completed email:");
        log.info("   Booking ID: {}", bookingId);
        log.info("   Payment ID: {}", paymentId);
        log.info("   Amount: {}", amount);
        log.info("   Transaction ID: {}", transactionId);
        log.info("   Subject: Payment Successful");
        log.info("   Body: Your payment has been processed successfully.");
    }

    /**
     * Mock: Send payment failed email
     */
    public void sendPaymentFailedEmail(String bookingId, String paymentId, String reason) {
        log.info("📧 [MOCK EMAIL] Sending payment failed email:");
        log.info("   Booking ID: {}", bookingId);
        log.info("   Payment ID: {}", paymentId);
        log.info("   Reason: {}", reason);
        log.info("   Subject: Payment Failed");
        log.info("   Body: Your payment failed. Reason: {}", reason);
    }

    /**
     * Mock: Send payment refunded email
     */
    public void sendPaymentRefundedEmail(String bookingId, String paymentId, String refundAmount, String reason) {
        log.info("📧 [MOCK EMAIL] Sending payment refunded email:");
        log.info("   Booking ID: {}", bookingId);
        log.info("   Payment ID: {}", paymentId);
        log.info("   Refund Amount: {}", refundAmount);
        log.info("   Reason: {}", reason);
        log.info("   Subject: Payment Refunded");
        log.info("   Body: Your payment has been refunded. Amount: {}", refundAmount);
    }
}
