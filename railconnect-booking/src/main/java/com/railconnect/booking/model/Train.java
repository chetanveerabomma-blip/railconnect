package com.railconnect.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trains")
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "train_number", nullable = false, unique = true, length = 10)
    private String trainNumber;

    @Column(name = "train_name", nullable = false, length = 100)
    private String trainName;

    @Column(name = "train_type", nullable = false, length = 50)
    private String trainType = "EXPRESS"; // VANDE_BHARAT, RAJDHANI, SHATABDI, EXPRESS

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_station_id", nullable = false)
    private Station sourceStation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_station_id", nullable = false)
    private Station destinationStation;

    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalTime arrivalTime;

    @Column(name = "duration_hours", nullable = false)
    private Double durationHours;

    @Column(name = "running_days", nullable = false)
    private String runningDays = "MON,TUE,WED,THU,FRI,SAT,SUN";

    private Boolean active = true;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("stopSequence ASC")
    @JsonIgnore
    private List<TrainRoute> routes = new ArrayList<>();

    public Train() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }

    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }

    public String getTrainType() { return trainType; }
    public void setTrainType(String trainType) { this.trainType = trainType; }

    public Station getSourceStation() { return sourceStation; }
    public void setSourceStation(Station sourceStation) { this.sourceStation = sourceStation; }

    public Station getDestinationStation() { return destinationStation; }
    public void setDestinationStation(Station destinationStation) { this.destinationStation = destinationStation; }

    public LocalTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalTime departureTime) { this.departureTime = departureTime; }

    public LocalTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public Double getDurationHours() { return durationHours; }
    public void setDurationHours(Double durationHours) { this.durationHours = durationHours; }

    public String getRunningDays() { return runningDays; }
    public void setRunningDays(String runningDays) { this.runningDays = runningDays; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public List<TrainRoute> getRoutes() { return routes; }
    public void setRoutes(List<TrainRoute> routes) { this.routes = routes; }
}
