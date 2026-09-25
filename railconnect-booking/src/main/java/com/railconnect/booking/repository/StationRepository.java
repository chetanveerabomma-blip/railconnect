package com.railconnect.booking.repository;

import com.railconnect.booking.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findByCode(String code);
    Optional<Station> findByCodeIgnoreCase(String code);
    boolean existsByCode(String code);

    @org.springframework.data.jpa.repository.Query("SELECT s FROM Station s WHERE UPPER(s.code) = UPPER(:query) OR UPPER(s.name) LIKE CONCAT('%', UPPER(:query), '%') OR UPPER(s.city) LIKE CONCAT('%', UPPER(:query), '%')")
    java.util.List<Station> searchStations(@org.springframework.data.repository.query.Param("query") String query);
}
