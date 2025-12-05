package com.lul.train.application.service;

import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import com.lul.train.application.dto.mapper.StationDtoMapper;
import com.lul.train.application.dto.request.CreateStationRequest;
import com.lul.train.application.dto.request.UpdateStationRequest;
import com.lul.train.application.dto.response.StationDTO;
import com.lul.train.domain.station.aggregate.Station;
import com.lul.train.domain.station.repository.StationRepository;
import com.lul.train.domain.station.valueobject.StationCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StationApplicationService {
    private final StationRepository stationRepository;
    private final StationDtoMapper mapper;

    public StationDTO createStation(CreateStationRequest request) {
        StationCode stationCode = new StationCode(request.getStationCode());

        if (stationRepository.existsByStationCode(stationCode)) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS,
                    "Station code already exists");
        }

        Station station = Station.create(
                stationCode,
                request.getName(),
                request.getCity(),
                request.getProvince()
        );

        Station saved = stationRepository.save(station);
        log.info("Created station: {}", saved.getStationCode().getValue());

        return mapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public StationDTO getStation(String id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Station not found"));
        return mapper.toDTO(station);
    }

    @Transactional(readOnly = true)
    public List<StationDTO> getAllStations() {
        return stationRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public StationDTO updateStation(String id, UpdateStationRequest request) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Station not found"));

        station.updateInfo(request.getName(), request.getCity(), request.getProvince());

        Station updated = stationRepository.save(station);
        log.info("Updated station: {}", id);

        return mapper.toDTO(updated);
    }

    public void deleteStation(String id) {
        if (!stationRepository.findById(id).isPresent()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Station not found");
        }
        stationRepository.deleteById(id);
        log.info("Deleted station: {}", id);
    }
}