package com.railconnect.booking.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Dynamic Surge Pricing & Exponential Demand Curve Engine.
 *
 * Computes progressive flexi-fare multipliers based on real-time coach occupancy,
 * days until travel date, and peak festival windows.
 */
@Service
public class SurgePricingAlgorithm {

    public static class SurgeAnalysis {
        private final double baseMultiplier;
        private final double occupancySurchargePercent;
        private final double urgencyMultiplier;
        private final double finalEffectiveMultiplier;
        private final String pricingTierLabel;

        public SurgeAnalysis(double baseMultiplier, double occupancySurchargePercent, double urgencyMultiplier, double finalEffectiveMultiplier, String pricingTierLabel) {
            this.baseMultiplier = baseMultiplier;
            this.occupancySurchargePercent = occupancySurchargePercent;
            this.urgencyMultiplier = urgencyMultiplier;
            this.finalEffectiveMultiplier = finalEffectiveMultiplier;
            this.pricingTierLabel = pricingTierLabel;
        }

        public double getBaseMultiplier() { return baseMultiplier; }
        public double getOccupancySurchargePercent() { return occupancySurchargePercent; }
        public double getUrgencyMultiplier() { return urgencyMultiplier; }
        public double getFinalEffectiveMultiplier() { return finalEffectiveMultiplier; }
        public String getPricingTierLabel() { return pricingTierLabel; }
    }

    /**
     * Calculates dynamic fare adjustment:
     * - Occupancy < 50%: Tier 1 (1.00x Base)
     * - Occupancy 50% - 70%: Tier 2 (1.10x Moderate Demand)
     * - Occupancy 70% - 85%: Tier 3 (1.25x High Demand)
     * - Occupancy 85% - 100%: Tier 4 (1.40x Peak Demand)
     * - Last 24 hours urgency premium: +5%
     */
    public SurgeAnalysis calculateDynamicMultiplier(int totalSeats, int bookedSeats, LocalDate journeyDate) {
        if (totalSeats <= 0) {
            return new SurgeAnalysis(1.0, 0.0, 1.0, 1.0, "Standard Rate");
        }

        double occupancyRate = (double) bookedSeats / totalSeats;
        double occupancySurcharge = 0.0;
        String tier = "Standard Saver Rate";

        if (occupancyRate >= 0.85) {
            occupancySurcharge = 0.40;
            tier = "Tier 4: Peak Demand Surge (1.40x)";
        } else if (occupancyRate >= 0.70) {
            occupancySurcharge = 0.25;
            tier = "Tier 3: High Demand Surge (1.25x)";
        } else if (occupancyRate >= 0.50) {
            occupancySurcharge = 0.10;
            tier = "Tier 2: Moderate Flexi-Rate (1.10x)";
        } else {
            occupancySurcharge = 0.0;
            tier = "Tier 1: Early Bird Standard (1.00x)";
        }

        // Urgency factor: Days until departure
        double urgency = 1.0;
        if (journeyDate != null) {
            long daysToDeparture = ChronoUnit.DAYS.between(LocalDate.now(), journeyDate);
            if (daysToDeparture <= 1 && daysToDeparture >= 0) {
                urgency = 1.05; // 5% last-minute dispatch surcharge
            }
        }

        double finalMultiplier = (1.0 + occupancySurcharge) * urgency;
        finalMultiplier = Math.round(finalMultiplier * 100.0) / 100.0;

        return new SurgeAnalysis(1.0, occupancySurcharge * 100.0, urgency, finalMultiplier, tier);
    }
}
