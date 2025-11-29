package com.global.processor.service;

import com.global.processor.dto.SendRequest;
import com.global.processor.entity.LedgerEntryEntity;
import com.global.processor.entity.ReservationEntity;
import com.global.processor.kafka.PaymentEventProducer;
import com.global.processor.model.PaymentResult;
import com.global.processor.model.ReservationStatus;
import com.global.processor.repository.LedgerRepository;
import com.global.processor.repository.ReservationRepository;
import com.global.processor.util.IdempotencyUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PaymentService {

    private final ReservationRepository reservationRepository;
    private final LedgerRepository ledgerRepository;
    private final PaymentEventProducer eventProducer;

    public PaymentService(ReservationRepository reservationRepository,
                          LedgerRepository ledgerRepository,
                          PaymentEventProducer eventProducer) {
        this.reservationRepository = reservationRepository;
        this.ledgerRepository = ledgerRepository;
        this.eventProducer = eventProducer;
    }

    @Transactional
    public PaymentResult send(SendRequest req) {

        // Lookup by reservation or direct paymentId
        Optional<ReservationEntity> reservationOpt =
                req.getReservationId() != null ?
                        reservationRepository.findById(req.getReservationId()) :
                        reservationRepository.findByPaymentId(req.getPaymentId());

        if (reservationOpt.isEmpty()) {
            return PaymentResult.failed("Reservation not found");
        }

        ReservationEntity r = reservationOpt.get();

        // Idempotent check: already captured?
        if (r.getStatus() == ReservationStatus.CAPTURED) {
            return PaymentResult.ok(r.getTransactionId());
        }

        // Simulate PSP transaction
        String txId = IdempotencyUtil.safeUuid();

        r.setTransactionId(txId);
        r.setStatus(ReservationStatus.CAPTURED);
        reservationRepository.save(r);

        ledgerRepository.save(new LedgerEntryEntity(
                r.getPaymentId(),
                "SEND",
                "Captured " + r.getAmount() + " " + r.getCurrency()
        ));

        eventProducer.paymentSent(r.getPaymentId(), txId);

        return PaymentResult.ok(txId);
    }

    @Transactional
    public void release(String paymentId, String reservationId, String reason) {
        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown reservation"));

        // Idempotent release
        if (reservation.getStatus() == ReservationStatus.RELEASED) return;

        reservation.setStatus(ReservationStatus.RELEASED);
        reservationRepository.save(reservation);

        ledgerRepository.save(new LedgerEntryEntity(paymentId, "RELEASE", reason));

        eventProducer.reservationReleased(paymentId, reservationId);
    }
}
