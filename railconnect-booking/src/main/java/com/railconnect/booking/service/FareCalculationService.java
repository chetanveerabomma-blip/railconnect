package com.railconnect.booking.service;

import com.railconnect.booking.dto.FareCalculationDto;
import com.railconnect.booking.model.FareRule;
import com.railconnect.booking.repository.FareRuleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FareCalculationService {

    private final FareRuleRepository fareRuleRepository;

    public FareCalculationService(FareRuleRepository fareRuleRepository) {
        this.fareRuleRepository = fareRuleRepository;
    }

    /**
     * Exact Railway Fare Formula Implementation:
     * Base Fare + Reservation Charge + Service Charge + Tax - Discount = Final Fare
     */
    public FareCalculationDto calculateFare(String trainType, String coachType, Double distanceKm, int passengerCount, Double discountAmount) {
        if (distanceKm == null || distanceKm <= 0) {
            distanceKm = 300.0; // default minimum journey distance
        }
        if (passengerCount <= 0) {
            passengerCount = 1;
        }
        if (discountAmount == null || discountAmount < 0) {
            discountAmount = 0.0;
        }

        // 1. Fetch Admin-controlled fare rule from database
        Optional<FareRule> ruleOpt = fareRuleRepository.findByTrainTypeAndCoachType(trainType, coachType);

        double ratePerKm;
        double reservationCharge;
        double superfastCharge;
        double gstPercentage;

        if (ruleOpt.isPresent()) {
            FareRule rule = ruleOpt.get();
            ratePerKm = rule.getBaseFarePerKm();
            reservationCharge = rule.getReservationCharge();
            superfastCharge = rule.getSuperfastCharge();
            gstPercentage = rule.getGstPercentage();
        } else {
            // Default fallbacks based on class
            switch (coachType) {
                case "1A":
                    ratePerKm = 2.80; reservationCharge = 60.0; superfastCharge = 75.0; gstPercentage = 5.0; break;
                case "2A":
                    ratePerKm = 1.80; reservationCharge = 50.0; superfastCharge = 45.0; gstPercentage = 5.0; break;
                case "3A":
                    ratePerKm = 1.25; reservationCharge = 40.0; superfastCharge = 45.0; gstPercentage = 5.0; break;
                case "CC":
                    ratePerKm = 0.95; reservationCharge = 40.0; superfastCharge = 45.0; gstPercentage = 5.0; break;
                case "2S":
                    ratePerKm = 0.25; reservationCharge = 15.0; superfastCharge = 15.0; gstPercentage = 0.0; break;
                case "SL":
                default:
                    ratePerKm = 0.45; reservationCharge = 20.0; superfastCharge = 30.0; gstPercentage = 0.0; break;
            }
        }

        // Base Fare = Distance * Rate per Km * Passengers
        double rawBaseFare = Math.round(distanceKm * ratePerKm * passengerCount * 100.0) / 100.0;
        double totalReservation = reservationCharge * passengerCount;
        double totalService = superfastCharge * passengerCount;

        // Tax = GST percentage on (Base + Reservation + Service)
        double taxableSubtotal = rawBaseFare + totalReservation + totalService;
        double totalTax = Math.round((taxableSubtotal * (gstPercentage / 100.0)) * 100.0) / 100.0;

        // Final Fare = Base Fare + Reservation Charge + Service Charge + Tax - Discount
        double finalFare = Math.max(0.0, Math.round((taxableSubtotal + totalTax - discountAmount) * 100.0) / 100.0);

        return new FareCalculationDto(
                rawBaseFare,
                totalReservation,
                totalService,
                totalTax,
                discountAmount,
                finalFare,
                distanceKm,
                passengerCount
        );
    }
}
