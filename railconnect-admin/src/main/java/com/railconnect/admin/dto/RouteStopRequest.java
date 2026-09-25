package com.railconnect.admin.dto;

public class RouteStopRequest {
    private Long stationId;
    private Integer stopSequence;
    private Double distanceFromSourceKm;
    private String arrivalTime;
    private String departureTime;
    private Integer haltMinutes = 2;
    private Integer dayCount = 1;

    public RouteStopRequest() {}

    public Long getStationId() { return stationId; }
    public void setStationId(Long stationId) { this.stationId = stationId; }

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
