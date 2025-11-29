package com.global.settlement.service;

import com.global.settlement.entity.SettlementBatchEntity;
import com.global.settlement.entity.SettlementItemEntity;
import com.global.settlement.kafka.SettlementEventProducer;
import com.global.settlement.repository.BatchRepository;
import com.global.settlement.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.slf4j.*;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ConfirmationHandler {

    private static final Logger log = LoggerFactory.getLogger(ConfirmationHandler.class);

    private final BatchRepository batchRepository;
    private final ItemRepository itemRepository;
    private final SettlementEventProducer eventProducer;

    public ConfirmationHandler(BatchRepository batchRepository, ItemRepository itemRepository, SettlementEventProducer eventProducer) {
        this.batchRepository = batchRepository;
        this.itemRepository = itemRepository;
        this.eventProducer = eventProducer;
    }

    /**
     * Handle incoming confirmation from PSP: mark items settled or failed
     * minimal skeleton - real code will parse PSP payload
     */
    @Transactional
    public void handleConfirmation(UUID batchId, boolean success) {
        SettlementBatchEntity batch = batchRepository.findById(batchId).orElse(null);
        if (batch == null) {
            log.warn("Confirmation for unknown batch {}", batchId);
            return;
        }

        List<SettlementItemEntity> items = itemRepository.findByBatchId(batchId);
        if (success) {
            items.forEach(i -> {
                i.setStatus("SETTLED");
                i.setUpdatedAt(OffsetDateTime.now());
            });
            itemRepository.saveAll(items);

            batch.setStatus("CONFIRMED");
            batch.setUpdatedAt(OffsetDateTime.now());
            batchRepository.save(batch);

            eventProducer.publish(batch.getBatchReference(), "{\"event\":\"SettlementCompleted\",\"batchId\":\"" + batch.getId() + "\"}");
        } else {
            items.forEach(i -> {
                i.setStatus("FAILED");
                i.setFailureReason("PSP reported failure");
                i.setUpdatedAt(OffsetDateTime.now());
            });
            itemRepository.saveAll(items);

            batch.setStatus("FAILED");
            batch.setUpdatedAt(OffsetDateTime.now());
            batchRepository.save(batch);

            eventProducer.publish(batch.getBatchReference(), "{\"event\":\"SettlementFailed\",\"batchId\":\"" + batch.getId() + "\"}");
        }
    }
}
