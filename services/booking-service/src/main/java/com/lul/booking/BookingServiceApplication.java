package com.lul.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "com.lul.booking",
        "com.lul.common.core"
})
@EntityScan(basePackages = {
        "com.lul.booking.infrastructure.persistence.entity",
        "com.lul.common.core.infrastructure.persistence"
})
@EnableJpaAuditing
@EnableKafka
@EnableScheduling
@EnableFeignClients(basePackages = "com.lul.booking.infrastructure.client")
public class BookingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookingServiceApplication.class, args);
    }
}
