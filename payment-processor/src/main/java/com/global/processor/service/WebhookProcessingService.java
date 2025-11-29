package com.global.processor.service;

import com.global.processor.entity.ReservationEntity;
import com.global.processor.kafka.PaymentEventProducer;
import com.global.processor.model.ReservationStatus;
import com.global.processor.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WebhookProcessingService {

    private final ReservationRepository reservationRepository;
    private final PaymentEventProducer eventProducer;

    public WebhookProcessingService(ReservationRepository reservationRepository,
                                    PaymentEventProducer eventProducer) {
        this.reservationRepository = reservationRepository;
        this.eventProducer = eventProducer;
    }

    @Transactional
    public void handleAsyncStatus(String paymentId, String status) {

        ReservationEntity reservation = reservationRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown paymentId"));

        switch (status.toUpperCase()) {
            case "SUCCESS" -> {
                reservation.setStatus(ReservationStatus.CAPTURED);
                reservationRepository.save(reservation);
                eventProducer.paymentSent(paymentId, reservation.getTransactionId());
            }
            case "FAILED" -> {
                reservation.setStatus(ReservationStatus.FAILED);
                reservationRepository.save(reservation);
                eventProducer.paymentFailed(paymentId, "Async PSP reported failure");
            }
            default -> throw new IllegalArgumentException("Unknown PSP status");
        }
    }
}
