package com.railconnect.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "train_routes")
public class TrainRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id", nullable = false)
    @JsonIgnoreProperties("routes")
    private Train train;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Column(name = "stop_sequence", nullable = false)
    private Integer stopSequence;

    @Column(name = "distance_from_source_km", nullable = false)
    private Double distanceFromSourceKm = 0.0;

    @Column(name = "arrival_time")
    private LocalTime arrivalTime;

    @Column(name = "departure_time")
    private LocalTime departureTime;

    @Column(name = "halt_minutes")
    private Integer haltMinutes = 2;

    @Column(name = "day_count")
    private Integer dayCount = 1;

    public TrainRoute() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Train getTrain() { return train; }
    public void setTrain(Train train) { this.train = train; }

    public Station getStation() { return station; }
    public void setStation(Station station) { this.station = station; }

    public Integer getStopSequence() { return stopSequence; }
    public void setStopSequence(Integer stopSequence) { this.stopSequence = stopSequence; }

    public Double getDistanceFromSourceKm() { return distanceFromSourceKm; }
    public void setDistanceFromSourceKm(Double distanceFromSourceKm) { this.distanceFromSourceKm = distanceFromSourceKm; }

    public LocalTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public LocalTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalTime departureTime) { this.departureTime = departureTime; }

    public Integer getHaltMinutes() { return haltMinutes; }
    public void setHaltMinutes(Integer haltMinutes) { this.haltMinutes = haltMinutes; }

    public Integer getDayCount() { return dayCount; }
    public void setDayCount(Integer dayCount) { this.dayCount = dayCount; }
}
