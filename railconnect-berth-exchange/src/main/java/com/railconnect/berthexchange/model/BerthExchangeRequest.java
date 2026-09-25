package com.railconnect.berthexchange.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.railconnect.booking.model.Booking;
import com.railconnect.booking.model.BookingPassenger;
import com.railconnect.booking.model.Train;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "berth_exchange_requests")
public class BerthExchangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requester_booking_id", nullable = false)
    @JsonIgnoreProperties({"user", "payment", "passengers"})
    private Booking requesterBooking;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requester_passenger_id", nullable = false)
    @JsonIgnoreProperties("booking")
    private BookingPassenger requesterPassenger;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "target_booking_id", nullable = false)
    @JsonIgnoreProperties({"user", "payment", "passengers"})
    private Booking targetBooking;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "target_passenger_id", nullable = false)
    @JsonIgnoreProperties("booking")
    private BookingPassenger targetPassenger;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @Column(name = "journey_date", nullable = false)
    private LocalDate journeyDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ExchangeStatus status = ExchangeStatus.REQUESTED;

    @Column(name = "request_reason")
    private String requestReason;

    @Column(name = "admin_notes")
    private String adminNotes;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public BerthExchangeRequest() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Booking getRequesterBooking() { return requesterBooking; }
    public void setRequesterBooking(Booking requesterBooking) { this.requesterBooking = requesterBooking; }

    public BookingPassenger getRequesterPassenger() { return requesterPassenger; }
    public void setRequesterPassenger(BookingPassenger requesterPassenger) { this.requesterPassenger = requesterPassenger; }

    public Booking getTargetBooking() { return targetBooking; }
    public void setTargetBooking(Booking targetBooking) { this.targetBooking = targetBooking; }

    public BookingPassenger getTargetPassenger() { return targetPassenger; }
    public void setTargetPassenger(BookingPassenger targetPassenger) { this.targetPassenger = targetPassenger; }

    public Train getTrain() { return train; }
    public void setTrain(Train train) { this.train = train; }

    public LocalDate getJourneyDate() { return journeyDate; }
    public void setJourneyDate(LocalDate journeyDate) { this.journeyDate = journeyDate; }

    public ExchangeStatus getStatus() { return status; }
    public void setStatus(ExchangeStatus status) { this.status = status; }

    public String getRequestReason() { return requestReason; }
    public void setRequestReason(String requestReason) { this.requestReason = requestReason; }

    public String getAdminNotes() { return adminNotes; }
    public void setAdminNotes(String adminNotes) { this.adminNotes = adminNotes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
