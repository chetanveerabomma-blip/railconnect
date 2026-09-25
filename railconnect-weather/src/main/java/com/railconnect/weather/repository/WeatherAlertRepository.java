package com.railconnect.weather.repository;

import com.railconnect.weather.model.WeatherAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherAlertRepository extends JpaRepository<WeatherAlert, Long> {
    List<WeatherAlert> findByStationIdOrderByRecordedAtDesc(Long stationId);
    Optional<WeatherAlert> findFirstByStationIdOrderByRecordedAtDesc(Long stationId);
}
