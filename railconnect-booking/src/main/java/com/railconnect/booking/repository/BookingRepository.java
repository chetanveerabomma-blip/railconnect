package com.railconnect.booking.repository;

import com.railconnect.booking.model.Booking;
import com.railconnect.booking.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByPnrNumber(String pnrNumber);
    boolean existsByPnrNumber(String pnrNumber);
    List<Booking> findByUserIdOrderByBookingDateDesc(Long userId);
    List<Booking> findByTrainIdAndJourneyDateAndStatusNot(Long trainId, LocalDate journeyDate, BookingStatus status);

    @Query("SELECT bp.seatId FROM BookingPassenger bp " +
           "JOIN bp.booking b " +
           "WHERE b.train.id = :trainId " +
           "AND b.journeyDate = :journeyDate " +
           "AND b.status != 'CANCELLED' " +
           "AND bp.seatId IS NOT NULL")
    Set<Long> findBookedSeatIds(@Param("trainId") Long trainId, @Param("journeyDate") LocalDate journeyDate);

    long countByStatus(BookingStatus status);
}
