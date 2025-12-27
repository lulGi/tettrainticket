package com.lul.booking.infrastructure.client;

import com.lul.booking.infrastructure.client.config.FeignClientConfig;
import com.lul.booking.infrastructure.client.dto.ReserveSeatRequest;
import com.lul.booking.infrastructure.client.dto.ScheduleDTO;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client for train-service REST API
 */
@FeignClient(
        name = "train-service",
        configuration = FeignClientConfig.class
)
public interface TrainServiceClient {

    /**
     * Get schedule by ID (for validation and price)
     * Saga Step 0: Validate schedule exists
     */
    @GetMapping("/schedules/{id}")
    @Retry(name = "trainService")
    ScheduleDTO getSchedule(@PathVariable("id") String scheduleId);

    /**
     * Reserve seats on a schedule (Saga Step 1)
     * Returns void on success, throws BusinessException on conflict/error
     */
    @PostMapping("/schedules/{id}/reserve")
    @Retry(name = "trainService")
    void reserveSeats(
            @PathVariable("id") String scheduleId,
            @RequestBody ReserveSeatRequest request
    );

    /**
     * Release seats on a schedule (Saga Compensation)
     * Must NOT throw exceptions (compensation flow)
     */
    @PostMapping("/schedules/{id}/release")
    @Retry(name = "trainService")
    void releaseSeats(
            @PathVariable("id") String scheduleId,
            @RequestBody ReserveSeatRequest request
    );
}
