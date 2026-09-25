package com.railconnect.seatengine.repository;

import com.railconnect.seatengine.model.BerthType;
import com.railconnect.seatengine.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByCoachId(Long coachId);
    List<Seat> findByCoachIdAndBerthType(Long coachId, BerthType berthType);
    Optional<Seat> findByCoachIdAndSeatNumber(Long coachId, Integer seatNumber);

    @Query("SELECT s FROM Seat s WHERE s.coach.trainId = :trainId AND s.coach.coachType = :coachType ORDER BY s.coach.coachNumber ASC, s.seatNumber ASC")
    List<Seat> findByTrainIdAndCoachType(@Param("trainId") Long trainId, @Param("coachType") String coachType);

    @Query("SELECT s FROM Seat s WHERE s.coach.trainId = :trainId ORDER BY s.coach.coachNumber ASC, s.seatNumber ASC")
    List<Seat> findByTrainId(@Param("trainId") Long trainId);
}
