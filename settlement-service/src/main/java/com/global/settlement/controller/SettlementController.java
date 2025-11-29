package com.global.settlement.controller;

import com.global.settlement.dto.BatchRequestDto;
import com.global.settlement.dto.BatchResponseDto;
import com.global.settlement.entity.SettlementBatchEntity;
import com.global.settlement.service.BatchBuilderService;
import com.global.settlement.service.SettlementExecutorService;
import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/settlement")
public class SettlementController {

    private static final Logger log = LoggerFactory.getLogger(SettlementController.class);

    private final BatchBuilderService batchBuilderService;
    private final SettlementExecutorService executor;

    public SettlementController(BatchBuilderService batchBuilderService, SettlementExecutorService executor) {
        this.batchBuilderService = batchBuilderService;
        this.executor = executor;
    }

    @PostMapping("/run")
    public ResponseEntity<?> runBatch(@RequestBody BatchRequestDto req) {
        Optional<SettlementBatchEntity> b = batchBuilderService.buildBatch(req.getCurrency(), req.getPsp(), req.getWindow());
        if (b.isEmpty()) {
            return ResponseEntity.accepted().body("No eligible items for batch");
        }
        SettlementBatchEntity batch = b.get();
        // start execution asynchronously in real system; for skeleton run synchronously
        executor.executeBatch(batch.getId());
        return ResponseEntity.ok(new BatchResponseDto(batch.getId(), batch.getStatus()));
    }

    @PostMapping("/batches/{id}/retry")
    public ResponseEntity<?> retryBatch(@PathVariable UUID id) {
        // minimal RBAC or check omitted
        executor.executeBatch(id);
        return ResponseEntity.ok("Retry scheduled/executed");
    }

    @PostMapping("/batches/{id}/force-confirm")
    public ResponseEntity<?> forceConfirm(@PathVariable UUID id) {
        // operator action - in real: check role, audit entry
        // For skeleton: call ConfirmationHandler - omitted here; simply return OK
        log.warn("Force confirm called for batch {}", id);
        return ResponseEntity.ok("Force confirm applied (skeleton)");
    }
}
