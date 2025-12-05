package com.lul.train.infrastructure.persistence.repository.impl;

import com.lul.train.domain.station.aggregate.Station;
import com.lul.train.domain.station.repository.StationRepository;
import com.lul.train.domain.station.valueobject.StationCode;
import com.lul.train.infrastructure.persistence.mapper.StationMapper;
import com.lul.train.infrastructure.persistence.repository.jpa.StationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StationRepositoryImpl implements StationRepository {
    private final StationJpaRepository jpaRepository;
    private final StationMapper mapper;

    @Override
    public Station save(Station station) {
        var entity = mapper.toEntity(station);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Station> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Station> findByStationCode(StationCode stationCode) {
        return jpaRepository.findByStationCode(stationCode.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<Station> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean existsByStationCode(StationCode stationCode) {
        return jpaRepository.existsByStationCode(stationCode.getValue());
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}