package com.lul.train.infrastructure.persistence.entity;


import com.lul.common.core.infrastructure.persistence.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "routes")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RouteEntity extends BaseJpaEntity {

    @Column(name = "route_code", nullable = false, length = 20)
    private String routeCode;

    @Column(name = "origin_station_id", nullable = false, length = 36)
    private String originStationId;

    @Column(name = "destination_station_id", nullable = false, length = 36)
    private String destinationStationId;

    @Column(name = "distance", nullable = false)
    private Integer distance;

    @Column(name = "estimated_duration", nullable = false)
    private Integer estimatedDuration;
}
