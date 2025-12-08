package com.lul.train.application.dto.mapper;

import com.lul.train.application.dto.response.ScheduleDTO;
import com.lul.train.domain.schedule.aggregate.Schedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScheduleDtoMapper {

    /**
     *
     * @param domain
     * @return
     */
    ScheduleDTO toDTO(Schedule domain);

}
