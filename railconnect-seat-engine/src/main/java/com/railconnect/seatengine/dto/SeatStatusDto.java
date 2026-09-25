package com.railconnect.seatengine.dto;

import com.railconnect.seatengine.model.BerthType;
import com.railconnect.seatengine.model.SeatState;

public class SeatStatusDto {
    private Long seatId;
    private Long coachId;
    private String coachNumber;
    private Integer seatNumber;
    private BerthType berthType;
    private Integer cabinNumber;
    private SeatState state;
    private Long remainingLockSeconds;

    public SeatStatusDto() {}

    public SeatStatusDto(Long seatId, Long coachId, String coachNumber, Integer seatNumber, BerthType berthType, Integer cabinNumber, SeatState state, Long remainingLockSeconds) {
        this.seatId = seatId;
        this.coachId = coachId;
        this.coachNumber = coachNumber;
        this.seatNumber = seatNumber;
        this.berthType = berthType;
        this.cabinNumber = cabinNumber;
        this.state = state;
        this.remainingLockSeconds = remainingLockSeconds;
    }

    public Long getSeatId() { return seatId; }
    public void setSeatId(Long seatId) { this.seatId = seatId; }

    public Long getCoachId() { return coachId; }
    public void setCoachId(Long coachId) { this.coachId = coachId; }

    public String getCoachNumber() { return coachNumber; }
    public void setCoachNumber(String coachNumber) { this.coachNumber = coachNumber; }

    public Integer getSeatNumber() { return seatNumber; }
    public void setSeatNumber(Integer seatNumber) { this.seatNumber = seatNumber; }

    public BerthType getBerthType() { return berthType; }
    public void setBerthType(BerthType berthType) { this.berthType = berthType; }

    public Integer getCabinNumber() { return cabinNumber; }
    public void setCabinNumber(Integer cabinNumber) { this.cabinNumber = cabinNumber; }

    public SeatState getState() { return state; }
    public void setState(SeatState state) { this.state = state; }

    public Long getRemainingLockSeconds() { return remainingLockSeconds; }
    public void setRemainingLockSeconds(Long remainingLockSeconds) { this.remainingLockSeconds = remainingLockSeconds; }
}
