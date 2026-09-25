package com.railconnect.admin.dto;

import com.railconnect.booking.model.TrainRoute;

public class RouteStopDto {
    private Long id;
    private Long trainId;
    private String trainNumber;
    private Long stationId;
    private String stationCode;
    private String stationName;
    private Integer stopSequence;
    private Double distanceFromSourceKm;
    private String arrivalTime;
    private String departureTime;
    private Integer haltMinutes;
    private Integer dayCount;

    public RouteStopDto() {}

    public RouteStopDto(TrainRoute route) {
        this.id = route.getId();
        if (route.getTrain() != null) {
            this.trainId = route.getTrain().getId();
            this.trainNumber = route.getTrain().getTrainNumber();
        }
        if (route.getStation() != null) {
            this.stationId = route.getStation().getId();
            this.stationCode = route.getStation().getCode();
            this.stationName = route.getStation().getName();
        }
        this.stopSequence = route.getStopSequence();
        this.distanceFromSourceKm = route.getDistanceFromSourceKm();
        this.arrivalTime = route.getArrivalTime() != null ? route.getArrivalTime().toString() : "--";
        this.departureTime = route.getDepartureTime() != null ? route.getDepartureTime().toString() : "--";
        this.haltMinutes = route.getHaltMinutes();
        this.dayCount = route.getDayCount();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTrainId() { return trainId; }
    public void setTrainId(Long trainId) { this.trainId = trainId; }

    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }

    public Long getStationId() { return stationId; }
    public void setStationId(Long stationId) { this.stationId = stationId; }

    public String getStationCode() { return stationCode; }
    public void setStationCode(String stationCode) { this.stationCode = stationCode; }

    public String getStationName() { return stationName; }
    public void setStationName(String stationName) { this.stationName = stationName; }

    public Integer getStopSequence() { return stopSequence; }
    public void setStopSequence(Integer stopSequence) { this.stopSequence = stopSequence; }

    public Double getDistanceFromSourceKm() { return distanceFromSourceKm; }
    public void setDistanceFromSourceKm(Double distanceFromSourceKm) { this.distanceFromSourceKm = distanceFromSourceKm; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public Integer getHaltMinutes() { return haltMinutes; }
    public void setHaltMinutes(Integer haltMinutes) { this.haltMinutes = haltMinutes; }

    public Integer getDayCount() { return dayCount; }
    public void setDayCount(Integer dayCount) { this.dayCount = dayCount; }
}
