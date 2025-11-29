package com.global.processor.repository;

import com.global.processor.entity.ReservationEntity;
import com.global.processor.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<ReservationEntity, String> {

    Optional<ReservationEntity> findByPaymentId(String paymentId);

    Optional<ReservationEntity> findByIdAndStatus(String id, ReservationStatus status);
}
