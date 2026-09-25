package com.railconnect.booking.repository;

import com.railconnect.booking.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
    Optional<Train> findByTrainNumber(String trainNumber);
    List<Train> findByActiveTrue();

    @Query("SELECT DISTINCT t FROM Train t " +
           "JOIN t.routes r1 " +
           "JOIN t.routes r2 " +
           "WHERE r1.station.id = :sourceStationId " +
           "AND r2.station.id = :destStationId " +
           "AND r1.stopSequence < r2.stopSequence " +
           "AND t.active = true")
    List<Train> findDirectTrainsBetweenStations(@Param("sourceStationId") Long sourceStationId,
                                               @Param("destStationId") Long destStationId);
}
