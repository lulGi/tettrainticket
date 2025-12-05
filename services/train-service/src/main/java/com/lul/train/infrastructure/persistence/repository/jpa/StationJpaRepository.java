package com.lul.train.infrastructure.persistence.repository.jpa;

import com.lul.train.infrastructure.persistence.entity.StationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationJpaRepository extends JpaRepository<StationEntity, String> {
    Optional<StationEntity> findByStationCode(String stationCode);
    boolean existsByStationCode(String stationCode);
}
