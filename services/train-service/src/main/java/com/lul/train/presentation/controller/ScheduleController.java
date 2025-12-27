package com.lul.train.presentation.controller;

import com.lul.train.application.dto.request.CreateScheduleRequest;
import com.lul.train.application.dto.request.ReserveSeatRequest;
import com.lul.train.application.dto.request.UpdateScheduleRequest;
import com.lul.train.application.dto.response.ScheduleDTO;
import com.lul.train.application.service.ScheduleApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleApplicationService scheduleService;

    /**
     * Create new schedule
     * POST /api/schedules
     */
    @PostMapping
    public ResponseEntity<ScheduleDTO> createSchedule(@Valid @RequestBody CreateScheduleRequest request) {
        ScheduleDTO created = scheduleService.createSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get schedule by ID
     * GET /api/schedules/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> getSchedule(@PathVariable String id) {
        ScheduleDTO schedule = scheduleService.getScheduleById(id);
        return ResponseEntity.ok(schedule);
    }

    /**
     * Update schedule
     * PUT /api/schedules/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDTO> updateSchedule(
            @PathVariable String id,
            @Valid @RequestBody UpdateScheduleRequest request
    ) {
        ScheduleDTO updated = scheduleService.updateSchedule(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete schedule
     * DELETE /api/schedules/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search schedules by route and time range
     * GET /schedules/search?routeId=xxx&startTime=xxx&endTime=xxx
     * All parameters are optional
     */
    @GetMapping("/search")
    public ResponseEntity<List<ScheduleDTO>> searchSchedules(
            @RequestParam(required = false) String routeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime
    ) {
        List<ScheduleDTO> schedules = scheduleService.findSchedulesByRouteAndTime(routeId, startTime, endTime);
        return ResponseEntity.ok(schedules);
    }

    /**
     * Activate schedule
     * POST /api/schedules/{id}/activate
     */
    @PostMapping("/{id}/activate")
    public ResponseEntity<ScheduleDTO> activateSchedule(@PathVariable String id) {
        ScheduleDTO activated = scheduleService.activateSchedule(id);
        return ResponseEntity.ok(activated);
    }

    /**
     * Cancel schedule
     * POST /api/schedules/{id}/cancel
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ScheduleDTO> cancelSchedule(@PathVariable String id) {
        ScheduleDTO cancelled = scheduleService.cancelSchedule(id);
        return ResponseEntity.ok(cancelled);
    }

    /**
     * Reserve seats (FOR BOOKING-SERVICE - Saga Step 1)
     * POST /api/schedules/{id}/reserve
     */
    @PostMapping("/{id}/reserve")
    public ResponseEntity<ScheduleDTO> reserveSeats(
            @PathVariable String id,
            @Valid @RequestBody ReserveSeatRequest request
    ) {
        ScheduleDTO updated = scheduleService.reserveSeats(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Release seats (FOR BOOKING-SERVICE - Saga Compensation)
     * POST /api/schedules/{id}/release
     */
    @PostMapping("/{id}/release")
    public ResponseEntity<ScheduleDTO> releaseSeats(
            @PathVariable String id,
            @Valid @RequestBody ReserveSeatRequest request
    ) {
        ScheduleDTO updated = scheduleService.releaseSeats(id, request);
        return ResponseEntity.ok(updated);
    }
}
