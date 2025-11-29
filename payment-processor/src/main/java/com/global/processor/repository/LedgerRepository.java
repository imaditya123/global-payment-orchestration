package com.global.processor.repository;

import com.global.processor.entity.LedgerEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerRepository extends JpaRepository<LedgerEntryEntity, String> {
}
