package com.railconnect.booking.repository;

import com.railconnect.booking.model.TrainRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainRouteRepository extends JpaRepository<TrainRoute, Long> {
    List<TrainRoute> findByTrainIdOrderByStopSequenceAsc(Long trainId);
    Optional<TrainRoute> findByTrainIdAndStationId(Long trainId, Long stationId);
}
