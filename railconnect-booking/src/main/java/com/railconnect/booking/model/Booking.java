package com.railconnect.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.railconnect.auth.model.User;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pnr_number", nullable = false, unique = true, length = 10)
    private String pnrNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"passwordHash", "createdAt", "updatedAt"})
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_station_id", nullable = false)
    private Station fromStation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_station_id", nullable = false)
    private Station toStation;

    @Column(name = "journey_date", nullable = false)
    private LocalDate journeyDate;

    @Column(name = "booking_date")
    private LocalDateTime bookingDate = LocalDateTime.now();

    @Column(name = "coach_type", nullable = false, length = 20)
    private String coachType;

    @Column(name = "total_fare", nullable = false)
    private Double totalFare;

    @Column(name = "base_fare", nullable = false)
    private Double baseFare;

    @Column(name = "reservation_charge", nullable = false)
    private Double reservationCharge = 40.0;

    @Column(name = "service_charge", nullable = false)
    private Double serviceCharge = 30.0;

    @Column(name = "tax_amount", nullable = false)
    private Double taxAmount = 0.0;

    @Column(name = "discount_amount", nullable = false)
    private Double discountAmount = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BookingStatus status = BookingStatus.CONFIRMED;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "cancellation_date")
    private LocalDateTime cancellationDate;

    @Column(name = "refund_amount")
    private Double refundAmount = 0.0;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BookingPassenger> passengers = new ArrayList<>();

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Payment payment;

    public Booking() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPnrNumber() { return pnrNumber; }
    public void setPnrNumber(String pnrNumber) { this.pnrNumber = pnrNumber; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Train getTrain() { return train; }
    public void setTrain(Train train) { this.train = train; }

    public Station getFromStation() { return fromStation; }
    public void setFromStation(Station fromStation) { this.fromStation = fromStation; }

    public Station getToStation() { return toStation; }
    public void setToStation(Station toStation) { this.toStation = toStation; }

    public LocalDate getJourneyDate() { return journeyDate; }
    public void setJourneyDate(LocalDate journeyDate) { this.journeyDate = journeyDate; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public String getCoachType() { return coachType; }
    public void setCoachType(String coachType) { this.coachType = coachType; }

    public Double getTotalFare() { return totalFare; }
    public void setTotalFare(Double totalFare) { this.totalFare = totalFare; }

    public Double getBaseFare() { return baseFare; }
    public void setBaseFare(Double baseFare) { this.baseFare = baseFare; }

    public Double getReservationCharge() { return reservationCharge; }
    public void setReservationCharge(Double reservationCharge) { this.reservationCharge = reservationCharge; }

    public Double getServiceCharge() { return serviceCharge; }
    public void setServiceCharge(Double serviceCharge) { this.serviceCharge = serviceCharge; }

    public Double getTaxAmount() { return taxAmount; }
    public void setTaxAmount(Double taxAmount) { this.taxAmount = taxAmount; }

    public Double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Double discountAmount) { this.discountAmount = discountAmount; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }

    public LocalDateTime getCancellationDate() { return cancellationDate; }
    public void setCancellationDate(LocalDateTime cancellationDate) { this.cancellationDate = cancellationDate; }

    public Double getRefundAmount() { return refundAmount; }
    public void setRefundAmount(Double refundAmount) { this.refundAmount = refundAmount; }

    public List<BookingPassenger> getPassengers() { return passengers; }
    public void setPassengers(List<BookingPassenger> passengers) { this.passengers = passengers; }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }
}
