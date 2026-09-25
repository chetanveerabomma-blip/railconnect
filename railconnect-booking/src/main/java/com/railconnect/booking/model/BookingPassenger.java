package com.railconnect.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "booking_passengers")
public class BookingPassenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    @JsonIgnoreProperties("passengers")
    private Booking booking;

    @Column(name = "seat_id")
    private Long seatId;

    @Column(name = "passenger_name", nullable = false, length = 100)
    private String passengerName;

    @Column(name = "passenger_age", nullable = false)
    private Integer passengerAge;

    @Column(name = "passenger_gender", nullable = false, length = 10)
    private String passengerGender;

    @Column(name = "berth_preference", length = 30)
    private String berthPreference = "NO_PREFERENCE";

    @Column(name = "allocated_berth_type", length = 30)
    private String allocatedBerthType;

    @Column(name = "allocated_coach", length = 10)
    private String allocatedCoach;

    @Column(name = "allocated_seat_number")
    private Integer allocatedSeatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BookingStatus status = BookingStatus.CONFIRMED;

    public BookingPassenger() {}

    public BookingPassenger(String passengerName, Integer passengerAge, String passengerGender, String berthPreference) {
        this.passengerName = passengerName;
        this.passengerAge = passengerAge;
        this.passengerGender = passengerGender;
        this.berthPreference = berthPreference;
        this.status = BookingStatus.CONFIRMED;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    public Long getSeatId() { return seatId; }
    public void setSeatId(Long seatId) { this.seatId = seatId; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public Integer getPassengerAge() { return passengerAge; }
    public void setPassengerAge(Integer passengerAge) { this.passengerAge = passengerAge; }

    public String getPassengerGender() { return passengerGender; }
    public void setPassengerGender(String passengerGender) { this.passengerGender = passengerGender; }

    public String getBerthPreference() { return berthPreference; }
    public void setBerthPreference(String berthPreference) { this.berthPreference = berthPreference; }

    public String getAllocatedBerthType() { return allocatedBerthType; }
    public void setAllocatedBerthType(String allocatedBerthType) { this.allocatedBerthType = allocatedBerthType; }

    public String getAllocatedCoach() { return allocatedCoach; }
    public void setAllocatedCoach(String allocatedCoach) { this.allocatedCoach = allocatedCoach; }

    public Integer getAllocatedSeatNumber() { return allocatedSeatNumber; }
    public void setAllocatedSeatNumber(Integer allocatedSeatNumber) { this.allocatedSeatNumber = allocatedSeatNumber; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}
