package com.global.settlement.service;

import com.global.settlement.entity.SettlementBatchEntity;
import com.global.settlement.entity.SettlementItemEntity;
import com.global.settlement.repository.BatchRepository;
import com.global.settlement.repository.ItemRepository;
import com.global.settlement.util.IdempotencyUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BatchBuilderService {

    private final ItemRepository itemRepository;
    private final BatchRepository batchRepository;

    public BatchBuilderService(ItemRepository itemRepository, BatchRepository batchRepository) {
        this.itemRepository = itemRepository;
        this.batchRepository = batchRepository;
    }

    /**
     * Find READY items for currency and window and create an idempotent batch
     */
    @Transactional
    public Optional<SettlementBatchEntity> buildBatch(String currency, String psp, OffsetDateTime scheduledWindow) {
        // idempotency key:
        String windowIso = scheduledWindow.toString();
        String batchRef = IdempotencyUtil.batchReferenceForWindow(currency, psp, windowIso);

        // If batch already exists - return
        Optional<SettlementBatchEntity> existing = batchRepository.findByBatchReference(batchRef);
        if (existing.isPresent()) return existing;

        // For example purposes, pick items with status READY and matching currency.
        List<SettlementItemEntity> candidates = itemRepository.findByStatusAndCurrency("READY", currency);

        if (candidates.isEmpty()) {
            return Optional.empty();
        }

        // group by vendor maybe - but here we include all into a single batch for the currency/psp/window
        UUID batchId = UUID.randomUUID();
        SettlementBatchEntity batch = new SettlementBatchEntity();
        batch.setId(batchId);
        batch.setBatchReference(batchRef);
        batch.setCurrency(currency);
        batch.setPsp(psp);
        batch.setStatus("CREATED");
        batch.setScheduledWindow(scheduledWindow);
        batch.setCreatedAt(OffsetDateTime.now());
        batch.setUpdatedAt(OffsetDateTime.now());
        batch.setItemCount(candidates.size());
        BigDecimal total = candidates.stream()
                .map(SettlementItemEntity::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        batch.setTotalAmount(total);

        batchRepository.save(batch);

        // Lock and mark items as INCLUDED (do in same transaction to avoid double-inclusion)
        List<SettlementItemEntity> toInclude = candidates.stream().limit(1000).collect(Collectors.toList());
        for (SettlementItemEntity it : toInclude) {
            it.setBatchId(batchId);
            it.setStatus("INCLUDED");
            it.setUpdatedAt(OffsetDateTime.now());
        }
        itemRepository.saveAll(toInclude);

        return Optional.of(batch);
    }
}
