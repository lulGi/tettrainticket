package com.lul.train.presentation.controller;

import com.lul.common.core.response.ApiResponse;
import com.lul.train.application.dto.request.CreateStationRequest;
import com.lul.train.application.dto.request.UpdateStationRequest;
import com.lul.train.application.dto.response.StationDTO;
import com.lul.train.application.service.StationApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stations")
@RequiredArgsConstructor
public class StationController {
    private final StationApplicationService stationService;

    @PostMapping
    public ResponseEntity<ApiResponse<StationDTO>> createStation(
            @RequestBody @Valid CreateStationRequest request) {
        StationDTO station = stationService.createStation(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(station, "Station created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StationDTO>> getStation(@PathVariable String id) {
        StationDTO station = stationService.getStation(id);
        return ResponseEntity.ok(ApiResponse.success(station));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StationDTO>>> getAllStations() {
        List<StationDTO> stations = stationService.getAllStations();
        return ResponseEntity.ok(ApiResponse.success(stations));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StationDTO>> updateStation(
            @PathVariable String id,
            @RequestBody @Valid UpdateStationRequest request) {
        StationDTO station = stationService.updateStation(id, request);
        return ResponseEntity.ok(ApiResponse.success(station, "Station updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStation(@PathVariable String id) {
        stationService.deleteStation(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Station deleted successfully"));
    }
}