package com.railconnect.booking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "fare_rules")
public class FareRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "train_type", nullable = false, length = 50)
    private String trainType;

    @Column(name = "coach_type", nullable = false, length = 20)
    private String coachType;

    @Column(name = "base_fare_per_km", nullable = false)
    private Double baseFarePerKm;

    @Column(name = "reservation_charge", nullable = false)
    private Double reservationCharge = 40.0;

    @Column(name = "superfast_charge", nullable = false)
    private Double superfastCharge = 30.0;

    @Column(name = "gst_percentage", nullable = false)
    private Double gstPercentage = 5.0;

    @Column(name = "tatkal_multiplier", nullable = false)
    private Double tatkalMultiplier = 1.30;

    public FareRule() {}

    public FareRule(String trainType, String coachType, Double baseFarePerKm, Double reservationCharge, Double superfastCharge, Double gstPercentage) {
        this.trainType = trainType;
        this.coachType = coachType;
        this.baseFarePerKm = baseFarePerKm;
        this.reservationCharge = reservationCharge;
        this.superfastCharge = superfastCharge;
        this.gstPercentage = gstPercentage;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
