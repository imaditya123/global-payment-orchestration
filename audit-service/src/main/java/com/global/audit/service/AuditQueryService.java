package com.global.audit.service;


import com.global.audit.entity.AuditEventEntity;
import com.global.audit.repository.AuditEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Service
public class AuditQueryService {


    private final AuditEventRepository repository;


    public AuditQueryService(AuditEventRepository repository) {
        this.repository = repository;
    }


    public List<AuditEventEntity> getTimelineForPayment(UUID paymentId) {
        return repository.findByPaymentIdOrderByCreatedAtAsc(paymentId);
    }


    public List<AuditEventEntity> getTraceByCorrelationId(String correlationId) {
        return repository.findByCorrelationIdOrderByCreatedAtAsc(correlationId);
    }


    public Page<AuditEventEntity> searchByEventType(String eventType, int page, int size) {
        Pageable p = PageRequest.of(page, size);
        return repository.findByEventType(eventType, p);
    }


    public Page<AuditEventEntity> getVendorHistory(String vendorId, Instant from, Instant to, int page, int size) {
        Pageable p = PageRequest.of(page, size);
        return repository.findByVendorIdAndCreatedAtBetweenOrderByCreatedAtDesc(vendorId, from, to, p);
    }
}