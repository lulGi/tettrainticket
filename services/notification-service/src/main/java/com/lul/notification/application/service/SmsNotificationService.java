package com.lul.notification.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsNotificationService {

    /**
     * Mock: Send booking created SMS
     */
    public void sendBookingCreatedSms(String userId, String bookingId) {
        log.info("📱 [MOCK SMS] Sending booking created SMS:");
        log.info("   To User: {}", userId);
        log.info("   Message: Your booking {} has been created. Complete payment within 15 minutes.", bookingId);
    }

    /**
     * Mock: Send booking confirmed SMS
     */
    public void sendBookingConfirmedSms(String userId, String bookingId) {
        log.info("📱 [MOCK SMS] Sending booking confirmed SMS:");
        log.info("   To User: {}", userId);
        log.info("   Message: Your booking {} has been confirmed. Ticket ready!", bookingId);
    }

    /**
     * Mock: Send booking cancelled SMS
     */
    public void sendBookingCancelledSms(String userId, String bookingId) {
        log.info("📱 [MOCK SMS] Sending booking cancelled SMS:");
        log.info("   To User: {}", userId);
        log.info("   Message: Your booking {} has been cancelled.", bookingId);
    }

    /**
     * Mock: Send payment completed SMS
     */
    public void sendPaymentCompletedSms(String bookingId, String amount) {
        log.info("📱 [MOCK SMS] Sending payment completed SMS:");
        log.info("   Booking ID: {}", bookingId);
        log.info("   Message: Payment of {} completed for booking {}.", amount, bookingId);
    }

    /**
     * Mock: Send payment failed SMS
     */
    public void sendPaymentFailedSms(String bookingId, String reason) {
        log.info("📱 [MOCK SMS] Sending payment failed SMS:");
        log.info("   Booking ID: {}", bookingId);
        log.info("   Message: Payment failed for booking {}. Reason: {}", bookingId, reason);
    }
}
