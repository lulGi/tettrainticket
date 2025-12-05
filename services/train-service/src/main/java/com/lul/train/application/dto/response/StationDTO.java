package com.lul.train.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StationDTO {
    private String id;
    private String stationCode;
    private String name;
    private String city;
    private String province;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}