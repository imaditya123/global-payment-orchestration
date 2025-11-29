package com.global.settlement.repository;

import com.global.settlement.entity.SettlementBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BatchRepository extends JpaRepository<SettlementBatchEntity, UUID> {
    Optional<SettlementBatchEntity> findByBatchReference(String batchReference);
}
