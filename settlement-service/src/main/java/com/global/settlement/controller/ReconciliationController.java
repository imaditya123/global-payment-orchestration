package com.global.settlement.controller;

import com.global.settlement.dto.ReconciliationReportDto;
import com.global.settlement.service.ReconciliationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/settlement/reconciliation")
public class ReconciliationController {

    private final ReconciliationService reconciliationService;

    public ReconciliationController(ReconciliationService reconciliationService) {
        this.reconciliationService = reconciliationService;
    }

    @GetMapping("/{date}")
    public ResponseEntity<ReconciliationReportDto> getReport(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        ReconciliationReportDto report = reconciliationService.reconcile(date);
        return ResponseEntity.ok(report);
    }
}
