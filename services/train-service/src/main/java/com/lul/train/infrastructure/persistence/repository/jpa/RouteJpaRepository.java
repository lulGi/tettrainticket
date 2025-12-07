package com.lul.train.infrastructure.persistence.repository.jpa;

import com.lul.train.infrastructure.persistence.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RouteJpaRepository extends JpaRepository<RouteEntity, String> {

    Optional<RouteEntity> findByRouteCode(String routeCode);

    List<RouteEntity> findByOriginStationId(String originStationId);

    List<RouteEntity> findByDestinationStationId(String destinationStationId);

}
