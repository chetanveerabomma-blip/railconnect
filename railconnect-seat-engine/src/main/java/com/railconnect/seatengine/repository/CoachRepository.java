package com.railconnect.seatengine.repository;

import com.railconnect.seatengine.model.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
    List<Coach> findByTrainId(Long trainId);
    List<Coach> findByTrainIdAndCoachType(Long trainId, String coachType);
    Optional<Coach> findByTrainIdAndCoachNumber(Long trainId, String coachNumber);
}
