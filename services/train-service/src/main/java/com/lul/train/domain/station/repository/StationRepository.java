package com.lul.train.domain.station.repository;

import com.lul.train.domain.station.aggregate.Station;
import com.lul.train.domain.station.valueobject.StationCode;

import java.util.List;
import java.util.Optional;

public interface StationRepository {

    Station save(Station station);
    Optional<Station> findById(String id);
    Optional<Station> findByStationCode(StationCode stationCode);
    List<Station> findAll();
    Boolean existsByStationCode(StationCode stationCode);
    void deleteById(String id);

}
