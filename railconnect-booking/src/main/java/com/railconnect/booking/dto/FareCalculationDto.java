package com.railconnect.booking.dto;

public class FareCalculationDto {
    private Double baseFare;
    private Double reservationCharge;
    private Double serviceCharge;
    private Double taxAmount;
    private Double discountAmount;
    private Double finalFare;
    private Double distanceKm;
    private int passengerCount;
    private String breakdownSummary;

    public FareCalculationDto() {}

    public FareCalculationDto(Double baseFare, Double reservationCharge, Double serviceCharge, Double taxAmount, Double discountAmount, Double finalFare, Double distanceKm, int passengerCount) {
        this.baseFare = baseFare;
        this.reservationCharge = reservationCharge;
        this.serviceCharge = serviceCharge;
        this.taxAmount = taxAmount;
        this.discountAmount = discountAmount;
        this.finalFare = finalFare;
        this.distanceKm = distanceKm;
        this.passengerCount = passengerCount;
        this.breakdownSummary = String.format("Base Fare (₹%.2f) + Reservation (₹%.2f) + Service (₹%.2f) + Tax (₹%.2f) - Discount (₹%.2f) = Total ₹%.2f",
                baseFare, reservationCharge, serviceCharge, taxAmount, discountAmount, finalFare);
    }

    public Double getBaseFare() { return baseFare; }
    public void setBaseFare(Double baseFare) { this.baseFare = baseFare; }

    public Double getReservationCharge() { return reservationCharge; }
    public void setReservationCharge(Double reservationCharge) { this.reservationCharge = reservationCharge; }

    public Double getServiceCharge() { return serviceCharge; }
    public void setServiceCharge(Double serviceCharge) { this.serviceCharge = serviceCharge; }

    public Double getTaxAmount() { return taxAmount; }
    public void setTaxAmount(Double taxAmount) { this.taxAmount = taxAmount; }

    public Double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Double discountAmount) { this.discountAmount = discountAmount; }

    public Double getFinalFare() { return finalFare; }
    public void setFinalFare(Double finalFare) { this.finalFare = finalFare; }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }

    public int getPassengerCount() { return passengerCount; }
    public void setPassengerCount(int passengerCount) { this.passengerCount = passengerCount; }

    public String getBreakdownSummary() { return breakdownSummary; }
    public void setBreakdownSummary(String breakdownSummary) { this.breakdownSummary = breakdownSummary; }
}
