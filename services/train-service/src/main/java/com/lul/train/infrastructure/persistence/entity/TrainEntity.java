package com.lul.train.infrastructure.persistence.entity;

import com.lul.common.core.infrastructure.persistence.BaseJpaEntity;
import com.lul.train.domain.train.valueobject.TrainType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "trains")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TrainEntity extends BaseJpaEntity {

    @Column(name = "train_number", nullable = false, unique = true, length = 10)
    private String trainNumber;

    @Column(name = "train_name", nullable = false, length = 100)
    private String trainName;

    @Column(name = "train_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TrainType trainType;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

}
