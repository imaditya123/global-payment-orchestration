package com.global.audit.entity;


import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "audit_events")
public class AuditEventEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "payment_id")
    private UUID paymentId;


    @Column(name = "vendor_id")
    private String vendorId;


    @Column(name = "event_type")
    private String eventType;


    @Column(name = "correlation_id")
    private String correlationId;


    @Column(name = "payload", columnDefinition = "jsonb")
    private String payload;


    @Column(name = "created_at")
    private Instant createdAt;


    public AuditEventEntity() {}


    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public UUID getPaymentId() { return paymentId; }
    public void setPaymentId(UUID paymentId) { this.paymentId = paymentId; }
    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}