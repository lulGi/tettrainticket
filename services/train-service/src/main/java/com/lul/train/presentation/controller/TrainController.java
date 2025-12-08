package com.lul.train.presentation.controller;

import com.lul.train.application.dto.request.CreateTrainRequest;
import com.lul.train.application.dto.request.UpdateTrainRequest;
import com.lul.train.application.dto.response.TrainDTO;
import com.lul.train.application.service.TrainApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
@RequiredArgsConstructor
public class TrainController {

    private final TrainApplicationService trainService;

    /**
     * Create new train
     * POST /api/trains
     */
    @PostMapping
    public ResponseEntity<TrainDTO> createTrain(@Valid @RequestBody CreateTrainRequest request) {
        TrainDTO created = trainService.createTrain(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get train by ID
     * GET /api/trains/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrainDTO> getTrain(@PathVariable String id) {
        TrainDTO train = trainService.getTrainById(id);
        return ResponseEntity.ok(train);
    }

    /**
     * Get train by train number
     * GET /api/trains/number/{trainNumber}
     */
    @GetMapping("/number/{trainNumber}")
    public ResponseEntity<TrainDTO> getTrainByNumber(@PathVariable String trainNumber) {
        TrainDTO train = trainService.getTrainByTrainNumber(trainNumber);
        return ResponseEntity.ok(train);
    }

    /**
     * Get all trains
     * GET /api/trains
     */
    @GetMapping
    public ResponseEntity<List<TrainDTO>> getAllTrains() {
        List<TrainDTO> trains = trainService.getAllTrains();
        return ResponseEntity.ok(trains);
    }

    /**
     * Update train
     * PUT /api/trains/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrainDTO> updateTrain(
            @PathVariable String id,
            @Valid @RequestBody UpdateTrainRequest request
    ) {
        TrainDTO updated = trainService.updateTrain(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete train
     * DELETE /api/trains/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable String id) {
        trainService.deleteTrain(id);
        return ResponseEntity.noContent().build();
    }
}
