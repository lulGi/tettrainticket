package com.lul.train.application.dto.mapper;

import com.lul.train.application.dto.response.StationDTO;
import com.lul.train.domain.station.aggregate.Station;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StationDtoMapper {

    @Mapping(target = "stationCode", expression = "java(station.getStationCode().getValue())")
    StationDTO toDTO(Station station);
}