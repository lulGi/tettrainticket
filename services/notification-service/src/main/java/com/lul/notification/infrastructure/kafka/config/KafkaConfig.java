package com.lul.notification.infrastructure.kafka.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    public static final String BOOKING_EVENTS_TOPIC = "booking-events";
    public static final String PAYMENT_EVENTS_TOPIC = "payment-events";
}
