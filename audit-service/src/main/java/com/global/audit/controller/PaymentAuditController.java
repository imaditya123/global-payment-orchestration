package com.global.audit.controller;


import com.global.audit.entity.AuditEventEntity;
import com.global.audit.service.AuditQueryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.Instant;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/audit")
public class PaymentAuditController {


    private final AuditQueryService queryService;


    public PaymentAuditController(AuditQueryService queryService) {
        this.queryService = queryService;
    }


    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<List<AuditEventEntity>> getPaymentTimeline(@PathVariable String paymentId) {
        try {
            UUID pid = UUID.fromString(paymentId);
            return ResponseEntity.ok(queryService.getTimelineForPayment(pid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/trace/{correlationId}")
    public ResponseEntity<List<AuditEventEntity>> getTrace(@PathVariable String correlationId) {
        return ResponseEntity.ok(queryService.getTraceByCorrelationId(correlationId));
    }


    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<?> getVendorHistory(
        @PathVariable String vendorId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        Instant f = (from == null) ? Instant.EPOCH : from;
        Instant t = (to == null) ? Instant.now() : to;
        return ResponseEntity.ok(queryService.getVendorHistory(vendorId, f, t, page, size));
    }


    @GetMapping("/search")
        public ResponseEntity<?> searchByEventType(@RequestParam String eventType, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(queryService.searchByEventType(eventType, page, size));
    }
}