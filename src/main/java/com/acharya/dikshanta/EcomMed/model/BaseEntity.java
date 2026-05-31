package com.acharya.dikshanta.EcomMed.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    @Id
    @UuidGenerator
    private UUID id;
    @Column(nullable = false, updatable = false)
    @CreationTimestamp()
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = true, updatable = true)
    private LocalDateTime updatedAt;


}
