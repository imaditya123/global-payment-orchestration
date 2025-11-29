package com.global.processor.controller;

import com.global.processor.dto.GenericResponse;
import com.global.processor.dto.ReserveRequest;
import com.global.processor.dto.ReserveResponse;
import com.global.processor.dto.SendRequest;
import com.global.processor.model.PaymentResult;
import com.global.processor.repository.ReservationRepository;
import com.global.processor.service.PaymentService;
import com.global.processor.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/processor")
public class ProcessorController {

    private final ReservationService reservationService;
    private final PaymentService paymentService;
    private final ReservationRepository reservationRepository;

    public ProcessorController(ReservationService reservationService,
                               PaymentService paymentService,
                               ReservationRepository reservationRepository) {
        this.reservationService = reservationService;
        this.paymentService = paymentService;
        this.reservationRepository = reservationRepository;
    }

    @PostMapping("/reserve")
    public ResponseEntity<ReserveResponse> reserve(@Valid @RequestBody ReserveRequest request) {
        return ResponseEntity.ok(reservationService.reserve(request));
    }

    @PostMapping("/send")
    public ResponseEntity<?> send(@Valid @RequestBody SendRequest req) {
        PaymentResult result = paymentService.send(req);

        if (!result.isSuccess()) {
            return ResponseEntity.status(500)
                    .body(new GenericResponse("FAILED", result.getErrorMessage()));
        }

        return ResponseEntity.ok(new GenericResponse("SENT", result.getTransactionId()));
    }

    @PostMapping("/release")
    public ResponseEntity<GenericResponse> release(@RequestBody ReleaseBody body) {
        paymentService.release(body.paymentId, body.reservationId, body.reason);
        return ResponseEntity.ok(new GenericResponse("RELEASED", "Reservation released"));
    }

    @GetMapping("/status/{paymentId}")
    public ResponseEntity<?> status(@PathVariable String paymentId) {
        var res = reservationRepository.findByPaymentId(paymentId)
                .orElse(null);

        if (res == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(res);
    }

    // Inner DTO for release
    public static class ReleaseBody {
        public String paymentId;
        public String reservationId;
        public String reason;
    }
}
