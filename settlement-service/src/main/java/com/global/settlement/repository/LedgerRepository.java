package com.global.settlement.repository;

import com.global.settlement.entity.LedgerEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerRepository extends JpaRepository<LedgerEntryEntity, Long> {
}
