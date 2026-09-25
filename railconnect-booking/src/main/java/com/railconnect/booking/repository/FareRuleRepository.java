package com.railconnect.booking.repository;

import com.railconnect.booking.model.FareRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FareRuleRepository extends JpaRepository<FareRule, Long> {
    Optional<FareRule> findByTrainTypeAndCoachType(String trainType, String coachType);
}
