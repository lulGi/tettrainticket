package com.lul.booking.infrastructure.kafka.consumer;

import com.lul.booking.application.service.BookingSagaService;
import com.lul.booking.infrastructure.kafka.config.KafkaConfig;
import com.lul.booking.infrastructure.kafka.event.PaymentCompletedEvent;
import com.lul.booking.infrastructure.kafka.event.PaymentFailedEvent;
import com.lul.booking.infrastructure.kafka.event.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final BookingSagaService sagaService;

    @KafkaListener(
            topics = KafkaConfig.PAYMENT_EVENTS_TOPIC,
            groupId = "booking-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumePaymentEvent(ConsumerRecord<String, PaymentEvent> record) {
        String key = record.key();
        PaymentEvent event = record.value();
        
        log.info("Received payment event with key: {}, type: {}", key, 
                 event != null ? event.getClass().getSimpleName() : "null");

        if (event instanceof PaymentCompletedEvent) {
            handlePaymentCompleted((PaymentCompletedEvent) event);
        } else if (event instanceof PaymentFailedEvent) {
            handlePaymentFailed((PaymentFailedEvent) event);
        } else {
            log.warn("Unknown payment event type: {}", event != null ? event.getClass().getName() : "null");
        }
    }

    private void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.info("Payment completed for booking: {}, payment: {}",
                event.getBookingId(), event.getPaymentId());

        try {
            sagaService.handlePaymentCompleted(event.getBookingId(), event.getPaymentId());
        } catch (Exception e) {
            log.error("Error handling payment completed event", e);
            // In production: send to DLQ or retry
        }
    }

    private void handlePaymentFailed(PaymentFailedEvent event) {
        log.info("Payment failed for booking: {}, reason: {}",
                event.getBookingId(), event.getReason());

        try {
            sagaService.handlePaymentFailed(event.getBookingId(), event.getReason());
        } catch (Exception e) {
            log.error("Error handling payment failed event", e);
            // In production: send to DLQ or retry
        }
    }
}