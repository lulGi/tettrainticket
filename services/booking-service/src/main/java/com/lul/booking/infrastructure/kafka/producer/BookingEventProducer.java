package com.lul.booking.infrastructure.kafka.producer;

import com.lul.booking.domain.booking.event.BookingEvent;
import com.lul.booking.infrastructure.kafka.config.KafkaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEventProducer {

    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    public void publishEvent(BookingEvent event) {
        String key = event.getBookingId();

        CompletableFuture<SendResult<String, BookingEvent>> future =
                kafkaTemplate.send(KafkaConfig.BOOKING_EVENTS_TOPIC, key, event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Published event: {} to topic: {} with key: {}",
                        event.getClass().getSimpleName(),
                        KafkaConfig.BOOKING_EVENTS_TOPIC,
                        key);
            } else {
                log.error("Failed to publish event: {} with key: {}",
                        event.getClass().getSimpleName(), key, ex);
            }
        });
    }
}