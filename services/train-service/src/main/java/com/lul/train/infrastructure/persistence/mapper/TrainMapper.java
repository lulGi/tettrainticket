package com.lul.train.infrastructure.persistence.mapper;

import com.lul.common.core.domain.BaseEntity;
import com.lul.train.domain.train.aggregate.Train;
import com.lul.train.infrastructure.persistence.entity.TrainEntity;
import org.mapstruct.Mapper;

import java.lang.reflect.Field;

/**
 * MapStruct mapper for Train aggregate <-> TrainEntity
 * Pattern: Simpler than Station because no Value Objects
 */
@Mapper(componentModel = "spring")
public interface TrainMapper {

    /**
     * Map domain Train to JPA TrainEntity
     * MapStruct auto-maps all matching field names
     */
    TrainEntity toEntity(Train domain);

    /**
     * Map Jpa entity to Domain train
     * Use factory method and reflection
     */
    default Train toDomain(TrainEntity entity) {
        if(entity == null) return null;

        Train train = Train.create(
          entity.getTrainNumber(),
          entity.getTrainName(),
          entity.getTrainType(),
          entity.getTotalSeats()
        );

        setBaseEntity(train,entity);
        return train;
    }

    /**
     * Helper method to set BaseEntity fields using reflection
     */
    default void setBaseEntity(Train domain, TrainEntity entity) {
        try {
            Field idField = BaseEntity.class.getDeclaredField("id");
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