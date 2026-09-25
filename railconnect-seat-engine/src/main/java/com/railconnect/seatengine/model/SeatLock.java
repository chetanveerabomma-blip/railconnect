package com.railconnect.seatengine.model;

import java.time.Instant;

public class SeatLock {
    private Long seatId;
    private Long userId;
    private Instant lockTime;
    private Instant expiryTime;
    private SeatState state;

    public SeatLock() {}

    public SeatLock(Long seatId, Long userId, long durationMinutes) {
        this.seatId = seatId;
        this.userId = userId;
        this.lockTime = Instant.now();
        this.expiryTime = this.lockTime.plusSeconds(durationMinutes * 60);
        this.state = SeatState.LOCKED;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiryTime);
    }

    public Long getSeatId() { return seatId; }
    public void setSeatId(Long seatId) { this.seatId = seatId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Instant getLockTime() { return lockTime; }
    public void setLockTime(Instant lockTime) { this.lockTime = lockTime; }

    public Instant getExpiryTime() { return expiryTime; }
    public void setExpiryTime(Instant expiryTime) { this.expiryTime = expiryTime; }

    public SeatState getState() { return state; }
    public void setState(SeatState state) { this.state = state; }

    public long getRemainingSeconds() {
        long remaining = expiryTime.getEpochSecond() - Instant.now().getEpochSecond();
        return Math.max(0, remaining);
    }
}
