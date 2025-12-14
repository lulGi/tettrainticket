package com.lul.payment.infrastructure.kafka.producer;

import com.lul.payment.domain.payment.event.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    private static final String TOPIC = "payment-events";

    public void publishEvent(PaymentEvent event) {
        String key = event.getPaymentId();

        log.info("publishing event to Kafka - Topic: {}, Key: {}, Event type: {}",
                                        TOPIC, key, event.getClass().getSimpleName());

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC,key,event);

        future.whenComplete((result, ex) ->{
            if (ex == null) {
                log.info("Event published successfully - Topic: {}, Partition: {}, Offset: {}",
                        TOPIC, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            } else {
                log.error("Failed to publish event to Kafka - Topic: {}, Key: {}", TOPIC, key, ex);
            }
        });
    }
}
