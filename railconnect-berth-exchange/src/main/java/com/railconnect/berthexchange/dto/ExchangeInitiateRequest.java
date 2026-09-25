package com.railconnect.berthexchange.dto;

import jakarta.validation.constraints.NotNull;

public class ExchangeInitiateRequest {
    @NotNull(message = "Requester booking ID is required")
    private Long requesterBookingId;

    @NotNull(message = "Requester passenger ID is required")
    private Long requesterPassengerId;

    @NotNull(message = "Target booking ID is required")
    private Long targetBookingId;

    @NotNull(message = "Target passenger ID is required")
    private Long targetPassengerId;

    private String reason;

    public ExchangeInitiateRequest() {}

    public Long getRequesterBookingId() { return requesterBookingId; }
    public void setRequesterBookingId(Long requesterBookingId) { this.requesterBookingId = requesterBookingId; }

    public Long getRequesterPassengerId() { return requesterPassengerId; }
    public void setRequesterPassengerId(Long requesterPassengerId) { this.requesterPassengerId = requesterPassengerId; }

    public Long getTargetBookingId() { return targetBookingId; }
    public void setTargetBookingId(Long targetBookingId) { this.targetBookingId = targetBookingId; }

    public Long getTargetPassengerId() { return targetPassengerId; }
    public void setTargetPassengerId(Long targetPassengerId) { this.targetPassengerId = targetPassengerId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
