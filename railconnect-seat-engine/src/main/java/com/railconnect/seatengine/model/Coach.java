package com.railconnect.seatengine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coaches")
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "train_id", nullable = false)
    private Long trainId;

    @Column(name = "coach_number", nullable = false, length = 10)
    private String coachNumber;

    @Column(name = "coach_type", nullable = false, length = 20)
    private String coachType;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats = 72;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Seat> seats = new ArrayList<>();

    public Coach() {}

    public Coach(Long trainId, String coachNumber, String coachType, Integer totalSeats) {
        this.trainId = trainId;
        this.coachNumber = coachNumber;
        this.coachType = coachType;
        this.totalSeats = totalSeats;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTrainId() { return trainId; }
    public void setTrainId(Long trainId) { this.trainId = trainId; }

    public String getCoachNumber() { return coachNumber; }
    public void setCoachNumber(String coachNumber) { this.coachNumber = coachNumber; }

    public String getCoachType() { return coachType; }
    public void setCoachType(String coachType) { this.coachType = coachType; }

    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }

    public List<Seat> getSeats() { return seats; }
    public void setSeats(List<Seat> seats) { this.seats = seats; }
}
