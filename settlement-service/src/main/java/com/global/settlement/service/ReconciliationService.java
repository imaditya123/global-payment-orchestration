package com.global.settlement.service;

import com.global.settlement.dto.ReconciliationReportDto;
import com.global.settlement.entity.SettlementItemEntity;
import com.global.settlement.repository.ItemRepository;
import org.slf4j.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ReconciliationService {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationService.class);
    private final ItemRepository itemRepository;

    public ReconciliationService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Very basic reconciliation skeleton: compare local SETTLED items with PSP report which is mocked.
     */
    public ReconciliationReportDto reconcile(LocalDate date) {
        // In real: fetch PSP report for date. For skeleton, just scan local SETTLED items.
        List<SettlementItemEntity> settled = itemRepository.findByStatusAndCurrency("SETTLED", "USD"); // placeholder
        List<String> issues = new ArrayList<>();
        // mock a mismatch check
        if (settled.size() % 2 == 1) {
            issues.add("Amount mismatch detected on sample check");
        }
        return new ReconciliationReportDto(date.toString(), issues);
    }
}
