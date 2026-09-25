package com.railconnect.seatengine.dto;

public class SeatLockResponse {
    private Long seatId;
    private String coachNumber;
    private Integer seatNumber;
    private boolean locked;
    private long remainingSeconds;
    private String message;

    public SeatLockResponse() {}

    public SeatLockResponse(Long seatId, String coachNumber, Integer seatNumber, boolean locked, long remainingSeconds, String message) {
        this.seatId = seatId;
        this.coachNumber = coachNumber;
        this.seatNumber = seatNumber;
        this.locked = locked;
        this.remainingSeconds = remainingSeconds;
        this.message = message;
    }

    public Long getSeatId() { return seatId; }
    public void setSeatId(Long seatId) { this.seatId = seatId; }

    public String getCoachNumber() { return coachNumber; }
    public void setCoachNumber(String coachNumber) { this.coachNumber = coachNumber; }

    public Integer getSeatNumber() { return seatNumber; }
    public void setSeatNumber(Integer seatNumber) { this.seatNumber = seatNumber; }

    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }

    public long getRemainingSeconds() { return remainingSeconds; }
    public void setRemainingSeconds(long remainingSeconds) { this.remainingSeconds = remainingSeconds; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
