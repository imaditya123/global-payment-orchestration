package com.global.settlement.repository;

import com.global.settlement.entity.SettlementItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<SettlementItemEntity, UUID> {

    List<SettlementItemEntity> findByStatusAndCurrency(String status, String currency);

    List<SettlementItemEntity> findByBatchId(UUID batchId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from SettlementItemEntity i where i.status = :status")
    List<SettlementItemEntity> lockByStatus(String status);
}
