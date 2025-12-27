package com.lul.train.presentation.controller;

import com.lul.train.application.dto.request.CreateRouteRequest;
import com.lul.train.application.dto.request.UpdateRouteRequest;
import com.lul.train.application.dto.response.RouteDTO;
import com.lul.train.application.service.RouteApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteApplicationService routeService;

    /**
     * Create new route
     * POST /api/routes
     */
    @PostMapping
    public ResponseEntity<RouteDTO> createRoute(@Valid @RequestBody CreateRouteRequest request) {
        RouteDTO created = routeService.createRoute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get route by ID
     * GET /api/routes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<RouteDTO> getRoute(@PathVariable String id) {
        RouteDTO route = routeService.getRouteById(id);
        return ResponseEntity.ok(route);
    }

    /**
     * Get route by route code
     * GET /api/routes/code/{routeCode}
     */
    @GetMapping("/code/{routeCode}")
    public ResponseEntity<RouteDTO> getRouteByCode(@PathVariable String routeCode) {
        RouteDTO route = routeService.getRouteByCode(routeCode);
        return ResponseEntity.ok(route);
    }

    /**
     * Get routes by origin station
     * GET /api/routes/origin/{originStationId}
     */
    @GetMapping("/origin/{originStationId}")
    public ResponseEntity<List<RouteDTO>> getRoutesByOrigin(@PathVariable String originStationId) {
        List<RouteDTO> routes = routeService.getRoutesByOriginStation(originStationId);
        return ResponseEntity.ok(routes);
    }

    /**
     * Get routes by destination station
     * GET /api/routes/destination/{destinationStationId}
     */
    @GetMapping("/destination/{destinationStationId}")
    public ResponseEntity<List<RouteDTO>> getRoutesByDestination(@PathVariable String destinationStationId) {
        List<RouteDTO> routes = routeService.getRoutesByDestinationStation(destinationStationId);
        return ResponseEntity.ok(routes);
    }

    /**
     * Get all routes
     * GET /api/routes
     */
    @GetMapping
    public ResponseEntity<List<RouteDTO>> getAllRoutes() {
        List<RouteDTO> routes = routeService.getAllRoutes();
        return ResponseEntity.ok(routes);
    }

    /**
     * Update route
     * PUT /api/routes/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<RouteDTO> updateRoute(
            @PathVariable String id,
            @Valid @RequestBody UpdateRouteRequest request
    ) {
        RouteDTO updated = routeService.updateRoute(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete route
     * DELETE /api/routes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable String id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }
}
