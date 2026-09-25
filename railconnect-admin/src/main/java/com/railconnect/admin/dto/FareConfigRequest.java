package com.railconnect.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FareConfigRequest {
    @NotBlank(message = "Train type is required")
    private String trainType;

    @NotBlank(message = "Coach type is required")
    private String coachType;

    @NotNull(message = "Base fare per km is required")
    private Double baseFarePerKm;

    private Double reservationCharge = 40.0;
    private Double superfastCharge = 30.0;
    private Double gstPercentage = 5.0;
    private Double tatkalMultiplier = 1.30;

    public FareConfigRequest() {}

    public String getTrainType() { return trainType; }
    public void setTrainType(String trainType) { this.trainType = trainType; }

    public String getCoachType() { return coachType; }
    public void setCoachType(String coachType) { this.coachType = coachType; }

    public Double getBaseFarePerKm() { return baseFarePerKm; }
    public void setBaseFarePerKm(Double baseFarePerKm) { this.baseFarePerKm = baseFarePerKm; }

    public Double getReservationCharge() { return reservationCharge; }
    public void setReservationCharge(Double reservationCharge) { this.reservationCharge = reservationCharge; }

    public Double getSuperfastCharge() { return superfastCharge; }
    public void setSuperfastCharge(Double superfastCharge) { this.superfastCharge = superfastCharge; }

    public Double getGstPercentage() { return gstPercentage; }
    public void setGstPercentage(Double gstPercentage) { this.gstPercentage = gstPercentage; }

    public Double getTatkalMultiplier() { return tatkalMultiplier; }
    public void setTatkalMultiplier(Double tatkalMultiplier) { this.tatkalMultiplier = tatkalMultiplier; }
}
