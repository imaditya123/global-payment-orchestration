package com.global.audit.repository;


import com.global.audit.entity.AuditEventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface AuditEventRepository extends JpaRepository<AuditEventEntity, Long> {
    List<AuditEventEntity> findByPaymentIdOrderByCreatedAtAsc(UUID paymentId);
    Page<AuditEventEntity> findByVendorIdAndCreatedAtBetweenOrderByCreatedAtDesc(String vendorId, java.time.Instant from, java.time.Instant to, Pageable pageable);
    List<AuditEventEntity> findByCorrelationIdOrderByCreatedAtAsc(String correlationId);
    Page<AuditEventEntity> findByEventType(String eventType, Pageable pageable);
}