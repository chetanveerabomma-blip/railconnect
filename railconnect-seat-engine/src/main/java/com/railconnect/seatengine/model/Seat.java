package com.railconnect.seatengine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coach_id", nullable = false)
    @JsonIgnoreProperties("seats")
    private Coach coach;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "berth_type", nullable = false, length = 30)
    private BerthType berthType;

    @Column(name = "cabin_number")
    private Integer cabinNumber = 1;

    public Seat() {}

    public Seat(Coach coach, Integer seatNumber, BerthType berthType, Integer cabinNumber) {
        this.coach = coach;
        this.seatNumber = seatNumber;
        this.berthType = berthType;
        this.cabinNumber = cabinNumber;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Coach getCoach() { return coach; }
    public void setCoach(Coach coach) { this.coach = coach; }

    public Integer getSeatNumber() { return seatNumber; }
    public void setSeatNumber(Integer seatNumber) { this.seatNumber = seatNumber; }

    public BerthType getBerthType() { return berthType; }
    public void setBerthType(BerthType berthType) { this.berthType = berthType; }

    public Integer getCabinNumber() { return cabinNumber; }
    public void setCabinNumber(Integer cabinNumber) { this.cabinNumber = cabinNumber; }
}
