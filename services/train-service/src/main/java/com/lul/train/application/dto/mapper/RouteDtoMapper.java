package com.lul.train.application.dto.mapper;

import com.lul.train.application.dto.response.RouteDTO;
import com.lul.train.domain.route.aggregate.Route;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RouteDtoMapper {

    /**
     * Domain → DTO (simple auto-mapping)
     */
    RouteDTO toDto(Route domain);
}
