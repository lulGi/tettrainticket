package com.lul.train.infrastructure.persistence.mapper;

import com.lul.common.core.domain.BaseEntity;
import com.lul.train.domain.schedule.aggregate.Schedule;
import com.lul.train.infrastructure.persistence.entity.ScheduleEntity;
import org.mapstruct.Mapper;

import java.lang.reflect.Field;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    /**
     * Domain → Entity (simple mapping, no VOs to extract)
     */
    ScheduleEntity toEntity(Schedule domain);

    /**
     * Entity → Domain (use factory + reflection)
     */
    default Schedule toDomain(ScheduleEntity entity) {
        if (entity == null) {
            return null;
        }

        // 1. Create new Schedule using factory method
        Schedule schedule = Schedule.create(
                entity.getTrainId(),
                entity.getRouteId(),
                entity.getDepartureTime(),
                entity.getArrivalTime(),
                entity.getAvailableSeats(),
                entity.getPrice()
        );

        // 2. Restore status (factory sets SCHEDULED by default)
        // Use reflection to set the status field
        try {
            Field statusField = Schedule.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(schedule, entity.getStatus());
        } catch (Exception e) {
            throw new RuntimeException("Failed to set status field", e);
        }

        // 3. Set BaseEntity fields (id, version, timestamps) via reflection
        setBaseEntityFields(schedule, entity);

        return schedule;
    }

    /**
     * Helper method to set BaseEntity fields using reflection
     */
    default void setBaseEntityFields(Schedule domain, ScheduleEntity entity) {
        try {
            Field idField = BaseEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(domain, entity.getId());

            Field versionField = BaseEntity.class.getDeclaredField("version");
            versionField.setAccessible(true);
            versionField.set(domain, entity.getVersion());

            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(domain, entity.getCreatedAt());

            Field updatedAtField = BaseEntity.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(domain, entity.getUpdatedAt());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set BaseEntity fields via reflection", e);
        }
    }
}
