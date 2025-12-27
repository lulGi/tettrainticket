package com.lul.notification.infrastructure.kafka.consumer;

import com.lul.notification.application.service.EmailNotificationService;
import com.lul.notification.application.service.SmsNotificationService;
import com.lul.notification.infrastructure.kafka.config.KafkaConfig;
import com.lul.notification.infrastructure.kafka.event.BookingCancelledEvent;
import com.lul.notification.infrastructure.kafka.event.BookingConfirmedEvent;
import com.lul.notification.infrastructure.kafka.event.BookingCreatedEvent;
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
public class BookingEventConsumer {

    private final EmailNotificationService emailService;
    private final SmsNotificationService smsService;

    @KafkaListener(
            topics = KafkaConfig.BOOKING_EVENTS_TOPIC,
            groupId = "notification-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeBookingEvent(
            @Payload Object event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key
    ) {
        log.info("🔔 Received booking event with key: {}, type: {}", key, event.getClass().getSimpleName());

        try {
            if (event instanceof BookingCreatedEvent) {
                handleBookingCreated((BookingCreatedEvent) event);
            } else if (event instanceof BookingConfirmedEvent) {
                handleBookingConfirmed((BookingConfirmedEvent) event);
            } else if (event instanceof BookingCancelledEvent) {
                handleBookingCancelled((BookingCancelledEvent) event);
            } else {
                log.warn("⚠️  Unknown booking event type: {}", event.getClass().getName());
            }
        } catch (Exception e) {
            log.error("❌ Error handling booking event: {}", event.getClass().getSimpleName(), e);
            // In production: send to DLQ or implement retry mechanism
        }
    }

    private void handleBookingCreated(BookingCreatedEvent event) {
        log.info("✅ Processing BookingCreatedEvent - bookingId: {}", event.getBookingId());

        // Send email notification
        emailService.sendBookingCreatedEmail(
                event.getUserId(),
                event.getBookingId(),
                event.getScheduleId(),
                event.getNumberOfSeats(),
                event.getTotalPrice().toString()
        );

        // Send SMS notification
        smsService.sendBookingCreatedSms(event.getUserId(), event.getBookingId());

        log.info("📨 Notifications sent for booking created: {}", event.getBookingId());
    }

    private void handleBookingConfirmed(BookingConfirmedEvent event) {
        log.info("✅ Processing BookingConfirmedEvent - bookingId: {}", event.getBookingId());

        // Send email notification
        emailService.sendBookingConfirmedEmail(
                event.getUserId(),
                event.getBookingId(),
                event.getPaymentId()
        );

        // Send SMS notification
        smsService.sendBookingConfirmedSms(event.getUserId(), event.getBookingId());

        log.info("📨 Notifications sent for booking confirmed: {}", event.getBookingId());
    }

    private void handleBookingCancelled(BookingCancelledEvent event) {
        log.info("✅ Processing BookingCancelledEvent - bookingId: {}", event.getBookingId());

        // Send email notification
        emailService.sendBookingCancelledEmail(
                event.getUserId(),
                event.getBookingId(),
                event.getReason()
        );

        // Send SMS notification
        smsService.sendBookingCancelledSms(event.getUserId(), event.getBookingId());

        log.info("📨 Notifications sent for booking cancelled: {}", event.getBookingId());
    }
}
