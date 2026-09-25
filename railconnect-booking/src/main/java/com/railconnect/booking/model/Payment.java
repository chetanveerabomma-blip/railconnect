package com.railconnect.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    @JsonIgnoreProperties("payment")
    private Booking booking;

    @Column(name = "transaction_id", nullable = false, unique = true, length = 100)
    private String transactionId;

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod; // UPI, CREDIT_CARD, DEBIT_CARD, NET_BANKING

    @Column(nullable = false)
    private Double amount;

    @Column(name = "payment_status", nullable = false, length = 30)
    private String paymentStatus = "SUCCESS"; // SUCCESS, FAILED, REFUNDED

    @Column(name = "payment_date")
    private LocalDateTime paymentDate = LocalDateTime.now();

    @Column(name = "gateway_response", length = 500)
    private String gatewayResponse;

    public Payment() {}

    public Payment(Booking booking, String transactionId, String paymentMethod, Double amount, String paymentStatus) {
        this.booking = booking;
        this.transactionId = transactionId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.paymentDate = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public String getGatewayResponse() { return gatewayResponse; }
    public void setGatewayResponse(String gatewayResponse) { this.gatewayResponse = gatewayResponse; }
}
