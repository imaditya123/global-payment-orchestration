package com.global.settlement.service;

import com.global.settlement.entity.LedgerEntryEntity;
import com.global.settlement.entity.SettlementBatchEntity;
import com.global.settlement.entity.SettlementItemEntity;
import com.global.settlement.kafka.SettlementEventProducer;
import com.global.settlement.repository.BatchRepository;
import com.global.settlement.repository.ItemRepository;
import com.global.settlement.repository.LedgerRepository;
import com.global.settlement.util.FileFormatUtil;
import jakarta.transaction.Transactional;
import org.slf4j.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SettlementExecutorService {

    private static final Logger log = LoggerFactory.getLogger(SettlementExecutorService.class);

    private final BatchRepository batchRepository;
    private final ItemRepository itemRepository;
    private final LedgerRepository ledgerRepository;
    private final SettlementEventProducer eventProducer;
    private final RestTemplate restTemplate = new RestTemplate();

    public SettlementExecutorService(BatchRepository batchRepository,
                                     ItemRepository itemRepository,
                                     LedgerRepository ledgerRepository,
                                     SettlementEventProducer eventProducer) {
        this.batchRepository = batchRepository;
        this.itemRepository = itemRepository;
        this.ledgerRepository = ledgerRepository;
        this.eventProducer = eventProducer;
    }

    /**
     * Execute the given batch: serialize, dispatch (HTTP example), mark items and emit events.
     */
    @Transactional
    public void executeBatch(UUID batchId) {
        SettlementBatchEntity batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Batch not found " + batchId));

        if (!"CREATED".equals(batch.getStatus()) && !"IN_PROGRESS".equals(batch.getStatus())) {
            log.info("Batch {} in status {}, skipping execution", batchId, batch.getStatus());
            return;
        }

        List<SettlementItemEntity> items = itemRepository.findByBatchId(batchId);
        if (items.isEmpty()) {
            log.warn("Batch {} has no items", batchId);
            batch.setStatus("FAILED");
            batch.setUpdatedAt(OffsetDateTime.now());
            batchRepository.save(batch);
            return;
        }

        batch.setStatus("IN_PROGRESS");
        batch.setUpdatedAt(OffsetDateTime.now());
        batchRepository.save(batch);

        String payload = FileFormatUtil.toCsv(items);

        try {
            // For this skeleton, use HTTP POST to PSP endpoint - in reality, lookup connector config.
            String pspEndpoint = "https://psp.example.com/settle"; // replace via config
            // Simulate HTTP dispatch (synchronous)
            // restTemplate.postForEntity(pspEndpoint, payload, String.class);

            // Mark items SENT
            List<SettlementItemEntity> updated = items.stream().peek(it -> {
                it.setStatus("SENT");
                it.setUpdatedAt(OffsetDateTime.now());
            }).collect(Collectors.toList());
            itemRepository.saveAll(updated);

            batch.setStatus("SENT");
            batch.setUpdatedAt(OffsetDateTime.now());
            batchRepository.save(batch);

            // emit SettlementStarted event (simple JSON string)
            eventProducer.publish(batch.getBatchReference(), "{\"event\":\"SettlementStarted\",\"batchId\":\"" + batch.getId() + "\"}");

            // In synchronous PSP case assume immediate confirmation for skeleton:
            markBatchConfirmed(batch, updated);

        } catch (Exception ex) {
            log.error("Failed to dispatch batch {}", batchId, ex);
            batch.setStatus("FAILED");
            batch.setUpdatedAt(OffsetDateTime.now());
            batchRepository.save(batch);
            eventProducer.publish(batch.getBatchReference(), "{\"event\":\"SettlementFailed\",\"batchId\":\"" + batch.getId() + "\"}");
            // In real system: create dispatch_attempt record, schedule retry
        }
    }

    @Transactional
    protected void markBatchConfirmed(SettlementBatchEntity batch, List<SettlementItemEntity> items) {
        // mark items SETTLED, create ledger entries, mark batch CONFIRMED and emit SettlementCompleted
        for (SettlementItemEntity it : items) {
            it.setStatus("SETTLED");
            it.setUpdatedAt(OffsetDateTime.now());
        }
        itemRepository.saveAll(items);

        for (SettlementItemEntity it : items) {
            LedgerEntryEntity entry = new LedgerEntryEntity();
            entry.setPaymentId(it.getPaymentId());
            entry.setBatchId(it.getBatchId());
            entry.setAmount(it.getAmount());
            entry.setCurrency(it.getCurrency());
            entry.setEntryType("settlement");
            entry.setCreatedAt(OffsetDateTime.now());
            ledgerRepository.save(entry);
        }

        batch.setStatus("CONFIRMED");
        batch.setUpdatedAt(OffsetDateTime.now());
        batchRepository.save(batch);

        eventProducer.publish(batch.getBatchReference(), "{\"event\":\"SettlementCompleted\",\"batchId\":\"" + batch.getId() + "\"}");
    }
}
