package com.railconnect.berthexchange.repository;

import com.railconnect.berthexchange.model.BerthExchangeRequest;
import com.railconnect.berthexchange.model.ExchangeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExchangeRepository extends JpaRepository<BerthExchangeRequest, Long> {
    List<BerthExchangeRequest> findByStatus(ExchangeStatus status);

    @Query("SELECT r FROM BerthExchangeRequest r WHERE r.requesterBooking.user.id = :userId OR r.targetBooking.user.id = :userId ORDER BY r.createdAt DESC")
    List<BerthExchangeRequest> findByUserInvolved(@Param("userId") Long userId);

    @Query("SELECT r FROM BerthExchangeRequest r WHERE r.train.id = :trainId AND r.journeyDate = :journeyDate AND r.status IN ('REQUESTED', 'PENDING_ADMIN')")
    List<BerthExchangeRequest> findActiveRequestsByTrainAndDate(@Param("trainId") Long trainId, @Param("journeyDate") LocalDate journeyDate);

    @Query("SELECT r FROM BerthExchangeRequest r WHERE (r.requesterPassenger.id = :passengerId OR r.targetPassenger.id = :passengerId) AND r.status IN ('REQUESTED', 'PENDING_ADMIN')")
    List<BerthExchangeRequest> findActiveRequestsForPassenger(@Param("passengerId") Long passengerId);

    long countByStatus(ExchangeStatus status);
}
