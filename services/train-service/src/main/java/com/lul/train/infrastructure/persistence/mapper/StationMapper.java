package com.lul.train.infrastructure.persistence.mapper;

import com.lul.train.domain.station.aggregate.Station;
import com.lul.train.domain.station.valueobject.StationCode;
import com.lul.train.infrastructure.persistence.entity.StationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for Station aggregate <-> StationEntity
 * Pattern: Same as user-service CredentialMapper
 */
@Mapper(componentModel = "spring")
public interface StationMapper {

    /**
     * Map domain Station to JPA StationEntity
     */
    @Mapping(target = "stationCode", expression = "java(domain.getStationCode().getValue())")
    StationEntity toEntity(Station domain);

    /**
     * Map JPA StationEntity to domain Station
     * Uses factory method and reflection (same pattern as CredentialMapper)
     */
    default Station toDomain(StationEntity entity) {
        if (entity == null) {
            return null;
        }

        // 1. Reconstruct StationCode Value Object
        StationCode stationCode = new StationCode(entity.getStationCode());

        // 2. Create Station using factory method (không dùng constructor)
        Station station = Station.create(
            stationCode,
            entity.getName(),
            entity.getCity(),
            entity.getProvince()
        );

        // 3. Set BaseEntity fields (id, version, createdAt, updatedAt)
        setBaseEntityFields(station, entity);

        return station;
    }

    /**
     * Helper method to set BaseEntity fields using reflection
     * (Same pattern as user-service CredentialMapper)
     */
    default void setBaseEntityFields(Station domain, StationEntity entity) {
        try {
            // Set id
            java.lang.reflect.Field idField = com.lul.common.core.domain.BaseEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(domain, entity.getId());

            // Set version
            java.lang.reflect.Field versionField = com.lul.common.core.domain.BaseEntity.class.getDeclaredField("version");
            versionField.setAccessible(true);
            versionField.set(domain, entity.getVersion());

            // Set createdAt
            java.lang.reflect.Field createdAtField = com.lul.common.core.domain.BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(domain, entity.getCreatedAt());

            // Set updatedAt
            java.lang.reflect.Field updatedAtField = com.lul.common.core.domain.BaseEntity.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(domain, entity.getUpdatedAt());
        } catch (Exception e) {
            throw new RuntimeException("Failed to set BaseEntity fields", e);
        }
    }
}
