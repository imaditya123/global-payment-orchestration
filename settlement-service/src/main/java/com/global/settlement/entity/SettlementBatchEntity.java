package com.global.settlement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "settlement_batches")
public class SettlementBatchEntity {

    @Id
    private UUID id;

    @Column(unique = true, length = 64)
    private String batchReference;

    @Column(length = 3)
    private String currency;

    private String psp;

    private String status; // CREATED, IN_PROGRESS, SENT, CONFIRMED, FAILED, RECONCILED

    @Column(precision = 18, scale = 4)
    private BigDecimal totalAmount;

    private Integer itemCount;

    private OffsetDateTime scheduledWindow;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    @Column(columnDefinition = "jsonb")
    private String metadata;

    // constructors, getters, setters
    public SettlementBatchEntity() {}

    // getters & setters omitted for brevity (or generate via Lombok)
    // ... add as needed
    // For brevity in this skeleton, generate via IDE or Lombok in real project.
    // Keep simple accessors below if desired.
    // ... (omitted)
    
    // --- minimal getters/setters used in services ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getBatchReference() { return batchReference; }
    public void setBatchReference(String batchReference) { this.batchReference = batchReference; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getPsp() { return psp; }
    public void setPsp(String psp) { this.psp = psp; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public Integer getItemCount() { return itemCount; }
    public void setItemCount(Integer itemCount) { this.itemCount = itemCount; }
    public OffsetDateTime getScheduledWindow() { return scheduledWindow; }
    public void setScheduledWindow(OffsetDateTime scheduledWindow) { this.scheduledWindow = scheduledWindow; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
}
