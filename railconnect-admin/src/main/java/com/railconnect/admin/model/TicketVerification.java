package com.railconnect.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.railconnect.auth.model.User;
import com.railconnect.booking.model.Booking;
import com.railconnect.booking.model.Station;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_verifications")
public class TicketVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id", nullable = false)
    @JsonIgnoreProperties({"user", "payment"})
    private Booking booking;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inspector_id", nullable = false)
    @JsonIgnoreProperties({"passwordHash"})
    private User inspector;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "station_id")
    private Station station;

    @Column(name = "verification_status", nullable = false, length = 30)
    private String verificationStatus = "VERIFIED"; // VERIFIED, ABSENT, FRAUDULENT, UNVERIFIED

    @Column(length = 255)
    private String comments;

    @Column(name = "verification_timestamp")
    private LocalDateTime verificationTimestamp = LocalDateTime.now();

    public TicketVerification() {}

    public TicketVerification(Booking booking, User inspector, Station station, String verificationStatus, String comments) {
        this.booking = booking;
        this.inspector = inspector;
        this.station = station;
        this.verificationStatus = verificationStatus;
        this.comments = comments;
        this.verificationTimestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    public User getInspector() { return inspector; }
    public void setInspector(User inspector) { this.inspector = inspector; }

    public Station getStation() { return station; }
    public void setStation(Station station) { this.station = station; }

    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public LocalDateTime getVerificationTimestamp() { return verificationTimestamp; }
    public void setVerificationTimestamp(LocalDateTime verificationTimestamp) { this.verificationTimestamp = verificationTimestamp; }
}
