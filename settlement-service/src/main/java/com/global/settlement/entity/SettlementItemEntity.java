package com.global.settlement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "settlement_items")
public class SettlementItemEntity {

    @Id
    private UUID id;

    @Column
    private UUID batchId;

    @Column
    private UUID paymentId;

    private String vendorId;

    @Column(precision = 18, scale = 4)
    private BigDecimal amount;

    @Column(length = 3)
    private String currency;

    private String status; // READY, INCLUDED, SENT, SETTLED, FAILED

    @Column(columnDefinition = "text")
    private String failureReason;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public SettlementItemEntity() {}

    // getters & setters (omitted for brevity)
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getBatchId() { return batchId; }
    public void setBatchId(UUID batchId) { this.batchId = batchId; }
    public UUID getPaymentId() { return paymentId; }
    public void setPaymentId(UUID paymentId) { this.paymentId = paymentId; }
    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
