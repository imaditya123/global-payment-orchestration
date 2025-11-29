package com.global.processor.entity;

import com.global.processor.model.ReservationStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(
        name = "reservations",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_reservation_payment", columnNames = {"payment_id"})
        }
)
public class ReservationEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @Column(name = "payment_id", nullable = false)
    private String paymentId;

    @Column(name = "vendor_id", nullable = false)
    private String vendorId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(columnDefinition = "jsonb")
    @Convert(converter = MetadataConverter.class)
    private Map<String, Object> metadata;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    // Getters & Setters

    public String getId() { return id; }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
