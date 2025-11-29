package com.global.orchestrator.service;

import com.global.orchestrator.entity.PaymentEntity;
import com.global.orchestrator.model.PaymentStatus;
import com.global.orchestrator.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentStateService {
    private final PaymentRepository repository;

    @Transactional
    public PaymentEntity createOrGet(PaymentEntity entity) {
        if (entity.getId() == null) entity.setId(UUID.randomUUID());
        return repository.save(entity);
    }

    public Optional<PaymentEntity> find(UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public PaymentEntity updateStatus(PaymentEntity e, PaymentStatus status) {
        e.setStatus(status);
        return repository.save(e);
    }
}
