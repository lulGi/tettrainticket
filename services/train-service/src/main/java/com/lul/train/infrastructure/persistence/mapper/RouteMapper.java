package com.lul.train.infrastructure.persistence.mapper;

import com.lul.common.core.domain.BaseEntity;
import com.lul.train.domain.route.aggregate.Route;
import com.lul.train.infrastructure.persistence.entity.RouteEntity;
import org.mapstruct.Mapper;

import java.lang.reflect.Field;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    /**
     *
     * @param domain
     * @return
     */
    RouteEntity toEntity(Route domain);


    default Route toDomain(RouteEntity entity) {
        if(entity == null) {
            return null;
        }

        Route route = Route.create(
                entity.getRouteCode(),
                entity.getOriginStationId(),
                entity.getDestinationStationId(),
                entity.getDistance(),
                entity.getEstimatedDuration()
        );
        setBaseEntity(route,entity);
        return  route;
    }

    default void setBaseEntity(Route domain, RouteEntity entity){
        try {
            Object[][] fields = {
                    {"id", entity.getId()},
                    {"version", entity.getVersion()},
                    {"createdAt", entity.getCreatedAt()},
                    {"updatedAt", entity.getUpdatedAt()}
            };

            Class<?> base = BaseEntity.class;

            for (Object[] f : fields) {
                Field field = base.getDeclaredField((String) f[0]);
                field.setAccessible(true);
                field.set(domain, f[1]);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to set BaseEntity fields", e);
        }

    }

}
