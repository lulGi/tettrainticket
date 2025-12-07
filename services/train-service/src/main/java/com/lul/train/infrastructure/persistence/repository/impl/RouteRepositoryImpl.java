package com.lul.train.infrastructure.persistence.repository.impl;

import com.lul.train.domain.route.aggregate.Route;
import com.lul.train.domain.route.repository.RouteRepository;
import com.lul.train.infrastructure.persistence.entity.RouteEntity;
import com.lul.train.infrastructure.persistence.mapper.RouteMapper;
import com.lul.train.infrastructure.persistence.repository.jpa.RouteJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RouteRepositoryImpl implements RouteRepository {

    private final RouteJpaRepository jpaRepository;
    private final RouteMapper mapper;

    @Override
    public Route save(Route route) {
        RouteEntity entity = mapper.toEntity(route);
        RouteEntity saved = jpaRepository.save(entity);
        return  mapper.toDomain(saved);
    }

    @Override
    public Optional<Route> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Route> findByRouteCode(String routeCode) {
        return jpaRepository.findByRouteCode(routeCode).map(mapper::toDomain);
    }

    @Override
    public List<Route> findByOriginStationId(String originStationId) {
        return jpaRepository.findByOriginStationId(originStationId)
                .stream().map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Route> findByDestinationStationId(String destinationStationId) {
        return jpaRepository.findByDestinationStationId(destinationStationId)
                .stream().map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Route> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void delete(Route route) {
        jpaRepository.deleteById(route.getId());
    }
}
