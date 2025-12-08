package com.lul.train.application.service;


import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import com.lul.train.application.dto.mapper.ScheduleDtoMapper;
import com.lul.train.application.dto.request.CreateScheduleRequest;
import com.lul.train.application.dto.request.ReserveSeatRequest;
import com.lul.train.application.dto.request.UpdateScheduleRequest;
import com.lul.train.application.dto.response.ScheduleDTO;
import com.lul.train.domain.schedule.aggregate.Schedule;
import com.lul.train.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleApplicationService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleDtoMapper dtoMapper;


    /**
     * Create new schedule
     * @param request
     * @return
     */
    @Transactional
    public ScheduleDTO createSchedule(CreateScheduleRequest request) {

        log.info("Createing schedule for train: {}, route: {}", request.getTrainId(), request.getRouteId());

        Schedule schedule = Schedule.create(
                request.getTrainId(),
                request.getRouteId(),
                request.getDepartureTime(),
                request.getArrivalTime(),
                request.getAvailableSeats(),
                request.getPrice()
        );

        Schedule saved = scheduleRepository.save(schedule);
        log.info("Schedule created with id: {}", saved.getId());

        return dtoMapper.toDTO(saved);
    }

    /**
     * Get schedule with id
     * @param id
     * @return
     */
    @Transactional
    public ScheduleDTO getScheduleById(String id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND, "Schedule not found with id: " + id)
        );

        return dtoMapper.toDTO(schedule);
    }

    /**
     * Update schedule information
     * @param id
     * @param request
     * @return
     */
    @Transactional
    public ScheduleDTO updateSchedule(String id, UpdateScheduleRequest request) {
        log.info("Updating schedule: {}", id);

        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND, "Schedule not found with id: " + id)
        );

        schedule.updateInfo(
                request.getDepartureTime(),
                request.getArrivalTime(),
                request.getAvailableSeats(),
                request.getPrice()
        );
        Schedule updated = scheduleRepository.save(schedule);
        log.info("Schedule updated: {}", id);

        return dtoMapper.toDTO(updated);

    }


    /**
     * Delete schedule
     */
    @Transactional
    public void deleteSchedule(String id) {
        log.info("Deleting schedule: {}", id);

        if (scheduleRepository.findById(id).isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Schedule not found with ID: " + id);
        }

        scheduleRepository.deleteById(id);
        log.info("Schedule deleted: {}", id);
    }

    /**
     * Find schedules by route and departure time range
     */
    @Transactional(readOnly = true)
    public List<ScheduleDTO> findSchedulesByRouteAndTime(
            String routeId,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        log.info("Finding schedules for route: {} between {} and {}", routeId, startTime, endTime);

        List<Schedule> schedules = scheduleRepository.findByRouteIdAndDepartureTimeBetween(
                routeId, startTime, endTime
        );

        return schedules.stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Activate schedule (make it available for booking)
     */
    @Transactional
    public ScheduleDTO activateSchedule(String id) {
        log.info("Activating schedule: {}", id);

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Schedule not found with ID: " + id));

        schedule.activate();
        Schedule updated = scheduleRepository.save(schedule);

        log.info("Schedule activated: {}", id);
        return dtoMapper.toDTO(updated);
    }

    /**
     * Cancel schedule
     */
    @Transactional
    public ScheduleDTO cancelSchedule(String id) {
        log.info("Cancelling schedule: {}", id);

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Schedule not found with ID: " + id));

        schedule.cancel();
        Schedule updated = scheduleRepository.save(schedule);

        log.info("Schedule cancelled: {}", id);
        return dtoMapper.toDTO(updated);
    }

    @Transactional
    public ScheduleDTO reserveSeats(String id, ReserveSeatRequest request) {

        log.info("Reserving {} seats for schedule: {}", request.getNumberOfSeats(), id);

        //Using pessimistic lock for race condition
        Schedule schedule = scheduleRepository.findByIdWithLock(id).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND, "Schedule not found with ID: " + id)
        );

        //Domain method handles validation and updates availableSeats
        schedule.reserveSeats(request.getNumberOfSeats());

        Schedule updated = scheduleRepository.save(schedule);

        return dtoMapper.toDTO(updated);
    }

    @Transactional
    public ScheduleDTO releaseSeats(String id, ReserveSeatRequest request) {

        log.info("Releasing {} seats for schedule: {}", request.getNumberOfSeats(), id);

        Schedule schedule = scheduleRepository.findByIdWithLock(id).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND, "Schedule not found with ID: " + id)
        );

        schedule.releaseSeats(request.getNumberOfSeats());

        Schedule updated = scheduleRepository.save(schedule);
        log.info("Released {} seats for schedule: {}. Available seats: {}",
                request.getNumberOfSeats(), id, updated.getAvailableSeats());

        return dtoMapper.toDTO(updated);
    }


}
