package com.global.processor.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;

@Entity
@Table(name = "ledger")
public class LedgerEntryEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @Column(name = "payment_id", nullable = false)
    private String paymentId;

    @Column(name = "action", nullable = false)
    private String action; // RESERVE, RELEASE, SEND

    @Column(name = "details", columnDefinition = "text")
    private String details;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    public LedgerEntryEntity() {}

    public LedgerEntryEntity(String paymentId, String action, String details) {
        this.paymentId = paymentId;
        this.action = action;
        this.details = details;
    }

    public String getId() { return id; }
    public String getPaymentId() { return paymentId; }
    public String getAction() { return action; }
    public String getDetails() { return details; }
    public Instant getCreatedAt() { return createdAt; }
}
