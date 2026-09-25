package com.railconnect.seatengine.dto;

import jakarta.validation.constraints.NotNull;

public class SeatLockRequest {
    @NotNull(message = "Seat ID is required")
    private Long seatId;

    private Long durationMinutes = 10L;

    public SeatLockRequest() {}

    public SeatLockRequest(Long seatId, Long durationMinutes) {
        this.seatId = seatId;
        this.durationMinutes = durationMinutes != null ? durationMinutes : 10L;
    }

    public Long getSeatId() { return seatId; }
    public void setSeatId(Long seatId) { this.seatId = seatId; }

    public Long getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Long durationMinutes) { this.durationMinutes = durationMinutes; }
}
