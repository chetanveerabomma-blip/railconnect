package com.railconnect.booking.dto;

import java.util.ArrayList;
import java.util.List;

public class TrainSearchResponse {
    private Long trainId;
    private String trainNumber;
    private String trainName;
    private String trainType;
    private String fromStationCode;
    private String fromStationName;
    private String toStationCode;
    private String toStationName;
    private String departureTime;
    private String arrivalTime;
    private Double durationHours;
    private Double distanceKm;
    private String runningDays;
    private List<ClassAvailabilityDto> availableClasses = new ArrayList<>();

    public TrainSearchResponse() {}

    public static class ClassAvailabilityDto {
        private String coachType;
        private int availableSeats;
        private Double fare;
        private String status; // AVAILABLE, RAC, WL

        public ClassAvailabilityDto() {}

        public ClassAvailabilityDto(String coachType, int availableSeats, Double fare, String status) {
            this.coachType = coachType;
            this.availableSeats = availableSeats;
            this.fare = fare;
            this.status = status;
        }

        public String getCoachType() { return coachType; }
        public void setCoachType(String coachType) { this.coachType = coachType; }

        public int getAvailableSeats() { return availableSeats; }
        public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

        public Double getFare() { return fare; }
        public void setFare(Double fare) { this.fare = fare; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public Long getTrainId() { return trainId; }
    public void setTrainId(Long trainId) { this.trainId = trainId; }

    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }

    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }

    public String getTrainType() { return trainType; }
    public void setTrainType(String trainType) { this.trainType = trainType; }

    public String getFromStationCode() { return fromStationCode; }
    public void setFromStationCode(String fromStationCode) { this.fromStationCode = fromStationCode; }

    public String getFromStationName() { return fromStationName; }
    public void setFromStationName(String fromStationName) { this.fromStationName = fromStationName; }

    public String getToStationCode() { return toStationCode; }
    public void setToStationCode(String toStationCode) { this.toStationCode = toStationCode; }

    public String getToStationName() { return toStationName; }
    public void setToStationName(String toStationName) { this.toStationName = toStationName; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public Double getDurationHours() { return durationHours; }
    public void setDurationHours(Double durationHours) { this.durationHours = durationHours; }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }

    public String getRunningDays() { return runningDays; }
    public void setRunningDays(String runningDays) { this.runningDays = runningDays; }

    public List<ClassAvailabilityDto> getAvailableClasses() { return availableClasses; }
    public void setAvailableClasses(List<ClassAvailabilityDto> availableClasses) { this.availableClasses = availableClasses; }
}
