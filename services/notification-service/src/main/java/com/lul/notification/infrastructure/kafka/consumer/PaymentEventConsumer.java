package com.lul.notification.infrastructure.kafka.consumer;

import com.lul.notification.application.service.EmailNotificationService;
import com.lul.notification.application.service.SmsNotificationService;
import com.lul.notification.infrastructure.kafka.config.KafkaConfig;
import com.lul.notification.infrastructure.kafka.event.PaymentCompletedEvent;
import com.lul.notification.infrastructure.kafka.event.PaymentFailedEvent;
import com.lul.notification.infrastructure.kafka.event.PaymentRefundedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final EmailNotificationService emailService;
    private final SmsNotificationService smsService;

    @KafkaListener(
            topics = KafkaConfig.PAYMENT_EVENTS_TOPIC,
            groupId = "notification-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumePaymentEvent(
            @Payload Object event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key
    ) {
        log.info("🔔 Received payment event with key: {}, type: {}", key, event.getClass().getSimpleName());

        try {
            if (event instanceof PaymentCompletedEvent) {
                handlePaymentCompleted((PaymentCompletedEvent) event);
            } else if (event instanceof PaymentFailedEvent) {
                handlePaymentFailed((PaymentFailedEvent) event);
            } else if (event instanceof PaymentRefundedEvent) {
                handlePaymentRefunded((PaymentRefundedEvent) event);
            } else {
                log.warn("⚠️  Unknown payment event type: {}", event.getClass().getName());
            }
        } catch (Exception e) {
            log.error("❌ Error handling payment event: {}", event.getClass().getSimpleName(), e);
            // In production: send to DLQ or implement retry mechanism
        }
    }

    private void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.info("✅ Processing PaymentCompletedEvent - paymentId: {}", event.getPaymentId());

        // Send email notification
        emailService.sendPaymentCompletedEmail(
                event.getBookingId(),
                event.getPaymentId(),
                event.getAmount().toString(),
                event.getTransactionId()
        );

        // Send SMS notification
        smsService.sendPaymentCompletedSms(event.getBookingId(), event.getAmount().toString());

        log.info("📨 Notifications sent for payment completed: {}", event.getPaymentId());
    }

    private void handlePaymentFailed(PaymentFailedEvent event) {
        log.info("✅ Processing PaymentFailedEvent - paymentId: {}", event.getPaymentId());

        // Send email notification
        emailService.sendPaymentFailedEmail(
                event.getBookingId(),
                event.getPaymentId(),
                event.getReason()
        );

        // Send SMS notification
        smsService.sendPaymentFailedSms(event.getBookingId(), event.getReason());

        log.info("📨 Notifications sent for payment failed: {}", event.getPaymentId());
    }

    private void handlePaymentRefunded(PaymentRefundedEvent event) {
        log.info("✅ Processing PaymentRefundedEvent - paymentId: {}", event.getPaymentId());

        // Send email notification
        emailService.sendPaymentRefundedEmail(
                event.getBookingId(),
                event.getPaymentId(),
                event.getRefundAmount().toString(),
                event.getReason()
        );

        log.info("📨 Notifications sent for payment refunded: {}", event.getPaymentId());
    }
}
