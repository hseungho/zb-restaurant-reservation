package com.zerobase.hseungho.restaurantreservation.global.util;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class BaseDateEntity {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = SeoulDateTime.now();
        this.updatedAt = SeoulDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = SeoulDateTime.now();
    }

}
