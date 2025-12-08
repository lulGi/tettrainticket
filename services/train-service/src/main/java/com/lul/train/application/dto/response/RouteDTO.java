package com.lul.train.application.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RouteDTO {
    private String id;
    private String routeCode;
    private String originStationId;
    private String destinationStationId;
    private Integer distance;
    private Integer estimatedDuration;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
