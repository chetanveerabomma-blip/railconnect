package com.railconnect.admin.repository;

import com.railconnect.admin.model.TicketVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketVerificationRepository extends JpaRepository<TicketVerification, Long> {
    List<TicketVerification> findByBookingId(Long bookingId);
    List<TicketVerification> findByInspectorIdOrderByVerificationTimestampDesc(Long inspectorId);
    List<TicketVerification> findTop50ByOrderByVerificationTimestampDesc();
}
