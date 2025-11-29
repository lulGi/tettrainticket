package com.lul.common.core.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Base JPA entity for infrastructure layer
 * Contains framework-specific JPA and Spring Data annotations
 * This is NOT part of the domain layer - it's infrastructure concern
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseJpaEntity {

    @Id
    @Column(name = "id", length = 36)
    protected String id;

    @Version
    @Column(name = "version")
    protected Long version;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseJpaEntity)) return false;
        BaseJpaEntity that = (BaseJpaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
