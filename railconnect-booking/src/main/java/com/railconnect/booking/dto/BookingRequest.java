package com.railconnect.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class BookingRequest {
    @NotNull(message = "Train ID is required")
    private Long trainId;

    @NotBlank(message = "From station is required")
    private String fromStationCode;

    @NotBlank(message = "To station is required")
    private String toStationCode;

    @NotBlank(message = "Journey date is required (YYYY-MM-DD)")
    private String journeyDate;

    @NotBlank(message = "Coach type is required (e.g. 3A, 2A, SL, CC)")
    private String coachType;

    @NotEmpty(message = "At least one passenger must be provided")
    private List<PassengerDetailDto> passengers = new ArrayList<>();

    private String paymentMethod = "UPI"; // UPI, CREDIT_CARD, DEBIT_CARD, NET_BANKING
    private Double discountAmount = 0.0;

    public BookingRequest() {}

    public static class PassengerDetailDto {
        private String name;
        private int age;
        private String gender;
        private String berthPreference; // LOWER, MIDDLE, UPPER, etc.
        private Long selectedSeatId;    // Optional if auto-allocated or manually picked

        public PassengerDetailDto() {}

        public PassengerDetailDto(String name, int age, String gender, String berthPreference, Long selectedSeatId) {
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.berthPreference = berthPreference;
            this.selectedSeatId = selectedSeatId;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }

        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }

        public String getBerthPreference() { return berthPreference; }
        public void setBerthPreference(String berthPreference) { this.berthPreference = berthPreference; }

        public Long getSelectedSeatId() { return selectedSeatId; }
        public void setSelectedSeatId(Long selectedSeatId) { this.selectedSeatId = selectedSeatId; }
    }

    public Long getTrainId() { return trainId; }
    public void setTrainId(Long trainId) { this.trainId = trainId; }

    public String getFromStationCode() { return fromStationCode; }
    public void setFromStationCode(String fromStationCode) { this.fromStationCode = fromStationCode; }

    public String getToStationCode() { return toStationCode; }
    public void setToStationCode(String toStationCode) { this.toStationCode = toStationCode; }

    public String getJourneyDate() { return journeyDate; }
    public void setJourneyDate(String journeyDate) { this.journeyDate = journeyDate; }

    public String getCoachType() { return coachType; }
    public void setCoachType(String coachType) { this.coachType = coachType; }

    public List<PassengerDetailDto> getPassengers() { return passengers; }
    public void setPassengers(List<PassengerDetailDto> passengers) { this.passengers = passengers; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Double discountAmount) { this.discountAmount = discountAmount; }
}
