package com.lul.common.core.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Pure domain base entity - framework agnostic
 * No JPA, Spring, or any framework dependencies
 */
@Getter
public abstract class BaseEntity {

    protected String id;
    protected Long version;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    protected BaseEntity() {
        this.id = UUID.randomUUID().toString();
        this.version = 0L;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    protected void incrementVersion() {
        this.version++;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
