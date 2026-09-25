package com.railconnect.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TrainManagementRequest {
    @NotBlank(message = "Train number is required")
    private String trainNumber;

    @NotBlank(message = "Train name is required")
    private String trainName;

    private String trainType = "EXPRESS";

    @NotNull(message = "Source station ID is required")
    private Long sourceStationId;

    @NotNull(message = "Destination station ID is required")
    private Long destinationStationId;

    @NotBlank(message = "Departure time is required (HH:mm:ss)")
    private String departureTime;

    @NotBlank(message = "Arrival time is required (HH:mm:ss)")
    private String arrivalTime;

    @NotNull(message = "Duration hours is required")
    private Double durationHours;

    private String runningDays = "MON,TUE,WED,THU,FRI,SAT,SUN";
    private Boolean active = true;

    public TrainManagementRequest() {}

    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }

    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }

    public String getTrainType() { return trainType; }
    public void setTrainType(String trainType) { this.trainType = trainType; }

    public Long getSourceStationId() { return sourceStationId; }
    public void setSourceStationId(Long sourceStationId) { this.sourceStationId = sourceStationId; }

    public Long getDestinationStationId() { return destinationStationId; }
    public void setDestinationStationId(Long destinationStationId) { this.destinationStationId = destinationStationId; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public Double getDurationHours() { return durationHours; }
    public void setDurationHours(Double durationHours) { this.durationHours = durationHours; }

    public String getRunningDays() { return runningDays; }
    public void setRunningDays(String runningDays) { this.runningDays = runningDays; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
