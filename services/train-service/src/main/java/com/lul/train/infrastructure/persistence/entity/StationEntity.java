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
@Table(name = "stations")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class StationEntity extends BaseJpaEntity {

    @Column(name = "station_code", nullable = false, length = 5)
    private String stationCode;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "province", length = 50)
    private String province;

}
