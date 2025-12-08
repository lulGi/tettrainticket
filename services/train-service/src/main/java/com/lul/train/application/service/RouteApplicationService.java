package com.lul.train.application.service;

import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import com.lul.train.application.dto.mapper.RouteDtoMapper;
import com.lul.train.application.dto.request.CreateRouteRequest;
import com.lul.train.application.dto.request.UpdateRouteRequest;
import com.lul.train.application.dto.response.RouteDTO;
import com.lul.train.domain.route.aggregate.Route;
import com.lul.train.domain.route.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteApplicationService {

    private final RouteRepository routeRepository;
    private final RouteDtoMapper dtoMapper;

    /**
     * Create new route
     */
    @Transactional
    public RouteDTO createRoute(CreateRouteRequest request) {
        log.info("Creating route: {}", request.getRouteCode());

        Route route = Route.create(
                request.getRouteCode(),
                request.getOriginStationId(),
                request.getDestinationStationId(),
                request.getDistance(),
                request.getEstimatedDuration()
        );

        Route saved = routeRepository.save(route);
        log.info("Route created with ID: {}", saved.getId());

        return dtoMapper.toDto(saved);
    }

    /**
     * Get route by ID
     */
    @Transactional(readOnly = true)
    public RouteDTO getRouteById(String id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Route not found with ID: " + id));

        return dtoMapper.toDto(route);
    }

    /**
     * Get route by route code
     */
    @Transactional(readOnly = true)
    public RouteDTO getRouteByCode(String routeCode) {
        Route route = routeRepository.findByRouteCode(routeCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Route not found with code: " + routeCode));

        return dtoMapper.toDto(route);
    }

    /**
     * Find routes by origin station
     */
    @Transactional(readOnly = true)
    public List<RouteDTO> getRoutesByOriginStation(String originStationId) {
        return routeRepository.findByOriginStationId(originStationId)
                .stream()
                .map(dtoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Find routes by destination station
     */
    @Transactional(readOnly = true)
    public List<RouteDTO> getRoutesByDestinationStation(String destinationStationId) {
        return routeRepository.findByDestinationStationId(destinationStationId)
                .stream()
                .map(dtoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get all routes
     */
    @Transactional(readOnly = true)
    public List<RouteDTO> getAllRoutes() {
        return routeRepository.findAll()
                .stream()
                .map(dtoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Update route information
     */
    @Transactional
    public RouteDTO updateRoute(String id, UpdateRouteRequest request) {
        log.info("Updating route: {}", id);

        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Route not found with ID: " + id));

        route.updateInfo(request.getDistance(), request.getEstimatedDuration());

        Route updated = routeRepository.save(route);
        log.info("Route updated: {}", id);

        return dtoMapper.toDto(updated);
    }

    /**
     * Delete route
     */
    @Transactional
    public void deleteRoute(String id) {
        log.info("Deleting route: {}", id);

        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Route not found with ID: " + id));

        routeRepository.delete(route);
        log.info("Route deleted: {}", id);
    }
}
