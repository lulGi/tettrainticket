package com.lul.train.application.dto.mapper;

import com.lul.train.application.dto.response.TrainDTO;
import com.lul.train.domain.train.aggregate.Train;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainDtoMapper {

    /**
     * Domain → DTO (simple auto-mapping)
     */
    TrainDTO toDto(Train domain);
}
