package com.lul.train.application.service;

import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import com.lul.train.application.dto.mapper.TrainDtoMapper;
import com.lul.train.application.dto.request.CreateTrainRequest;
import com.lul.train.application.dto.request.UpdateTrainRequest;
import com.lul.train.application.dto.response.TrainDTO;
import com.lul.train.domain.train.aggregate.Train;
import com.lul.train.domain.train.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainApplicationService {

    private final TrainRepository trainRepository;
    private final TrainDtoMapper dtoMapper;

    /**
     * Create new train
     */
    @Transactional
    public TrainDTO createTrain(CreateTrainRequest request) {
        log.info("Creating train: {}", request.getTrainNumber());

        Train train = Train.create(
                request.getTrainNumber(),
                request.getTrainName(),
                request.getTrainType(),
                request.getTotalSeats()
        );

        Train saved = trainRepository.save(train);
        log.info("Train created with ID: {}", saved.getId());

        return dtoMapper.toDto(saved);
    }

    /**
     * Get train by ID
     */
    @Transactional(readOnly = true)
    public TrainDTO getTrainById(String id) {
        Train train = trainRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Train not found with ID: " + id));

        return dtoMapper.toDto(train);
    }

    /**
     * Get train by train number
     */
    @Transactional(readOnly = true)
    public TrainDTO getTrainByTrainNumber(String trainNumber) {
        Train train = trainRepository.findByTrainNumber(trainNumber)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Train not found with number: " + trainNumber));

        return dtoMapper.toDto(train);
    }

    /**
     * Get all trains
     */
    @Transactional(readOnly = true)
    public List<TrainDTO> getAllTrains() {
        return trainRepository.findAll()
                .stream()
                .map(dtoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Update train information
     */
    @Transactional
    public TrainDTO updateTrain(String id, UpdateTrainRequest request) {
        log.info("Updating train: {}", id);

        Train train = trainRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Train not found with ID: " + id));

        train.updateInfo(
                request.getTrainName(),
                request.getTrainType(),
                request.getTotalSeats()
        );

        Train updated = trainRepository.save(train);
        log.info("Train updated: {}", id);

        return dtoMapper.toDto(updated);
    }

    /**
     * Delete train
     */
    @Transactional
    public void deleteTrain(String id) {
        log.info("Deleting train: {}", id);

        Train train = trainRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Train not found with ID: " + id));

        trainRepository.delete(train);
        log.info("Train deleted: {}", id);
    }
}
