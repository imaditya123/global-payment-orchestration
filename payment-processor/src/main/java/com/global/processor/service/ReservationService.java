package com.global.processor.service;

import com.global.processor.dto.ReserveRequest;
import com.global.processor.dto.ReserveResponse;
import com.global.processor.entity.LedgerEntryEntity;
import com.global.processor.entity.ReservationEntity;
import com.global.processor.kafka.PaymentEventProducer;
import com.global.processor.model.ReservationStatus;
import com.global.processor.repository.LedgerRepository;
import com.global.processor.repository.ReservationRepository;
import com.global.processor.util.IdempotencyUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final LedgerRepository ledgerRepository;
    private final PaymentEventProducer eventProducer;

    public ReservationService(ReservationRepository reservationRepository,
                              LedgerRepository ledgerRepository,
                              PaymentEventProducer eventProducer) {
        this.reservationRepository = reservationRepository;
        this.ledgerRepository = ledgerRepository;
        this.eventProducer = eventProducer;
    }

    @Transactional
    public ReserveResponse reserve(ReserveRequest req) {

        // Check idempotency
        var existing = reservationRepository.findByPaymentId(req.getPaymentId());
        if (existing.isPresent()) {
            var r = existing.get();

            // Validate identical request
            if (IdempotencyUtil.mismatch(r.getAmount(), req.getAmount()) ||
                IdempotencyUtil.mismatch(r.getCurrency(), req.getCurrency())) {
                throw new IllegalStateException("Conflict: reservation already exists with different data.");
            }

            return new ReserveResponse(r.getStatus().name(), r.getId(), r.getAmount(), r.getCurrency());
        }

        // Create new reservation
        ReservationEntity entity = new ReservationEntity();
        entity.setPaymentId(req.getPaymentId());
        entity.setVendorId(req.getVendorId());
        entity.setAmount(req.getAmount());
        entity.setCurrency(req.getCurrency());
        entity.setStatus(ReservationStatus.RESERVED);
        entity.setMetadata(req.getMetadata());

        var saved = reservationRepository.save(entity);

        // Ledger audit
        ledgerRepository.save(new LedgerEntryEntity(
                saved.getPaymentId(),
                "RESERVE",
                "Reserved " + saved.getAmount() + " " + saved.getCurrency()
        ));

        // Emit event
        eventProducer.fundsReserved(saved.getPaymentId(), saved.getId());

        return new ReserveResponse("RESERVED", saved.getId(), saved.getAmount(), saved.getCurrency());
    }
}
