package com.global.orchestrator.entity;


import com.global.orchestrator.model.PaymentStatus;
import lombok.*;


import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payments")
public class PaymentEntity {
    @Id
    private UUID id;


    @Column(name = "vendor_id")
    private String vendorId;


    @Column(precision = 18, scale = 4)
    private java.math.BigDecimal amount;


    @Column(length = 3)
    private String currency;


    @Column(name = "target_currency", length = 3)
    private String targetCurrency;


    @Enumerated(EnumType.STRING)
    private PaymentStatus status;


    @Column(name = "retry_count")
    private Integer retryCount = 0;


    @Column(columnDefinition = "jsonb")
    private String metadata;


    @Column(name = "created_at")
    private OffsetDateTime createdAt;


    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;


    @PrePersist
    public void prePersist() {
    if (createdAt == null) createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }


    @PreUpdate
    public void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}