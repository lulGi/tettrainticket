package com.lul.train.domain.route.repository;

import com.lul.train.domain.route.aggregate.Route;

import java.util.List;
import java.util.Optional;

public interface RouteRepository {

    Route save(Route route);
    Optional<Route> findById(String id);
    Optional<Route> findByRouteCode(String routeCode);
    List<Route> findByOriginStationId(String originStationId);
    List<Route> findByDestinationStationId(String destinationStationId);
    List<Route> findAll();
    void delete(Route route);

}
