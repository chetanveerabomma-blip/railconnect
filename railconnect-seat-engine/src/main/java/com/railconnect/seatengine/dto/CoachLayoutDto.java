package com.railconnect.seatengine.dto;

import java.util.ArrayList;
import java.util.List;

public class CoachLayoutDto {
    private Long coachId;
    private Long trainId;
    private String coachNumber;
    private String coachType;
    private Integer totalSeats;
    private Integer availableCount;
    private Integer lockedCount;
    private Integer bookedCount;
    private List<SeatStatusDto> seats = new ArrayList<>();

    public CoachLayoutDto() {}

    public Long getCoachId() { return coachId; }
    public void setCoachId(Long coachId) { this.coachId = coachId; }

    public Long getTrainId() { return trainId; }
    public void setTrainId(Long trainId) { this.trainId = trainId; }

    public String getCoachNumber() { return coachNumber; }
    public void setCoachNumber(String coachNumber) { this.coachNumber = coachNumber; }

    public String getCoachType() { return coachType; }
    public void setCoachType(String coachType) { this.coachType = coachType; }

    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }

    public Integer getAvailableCount() { return availableCount; }
    public void setAvailableCount(Integer availableCount) { this.availableCount = availableCount; }

    public Integer getLockedCount() { return lockedCount; }
    public void setLockedCount(Integer lockedCount) { this.lockedCount = lockedCount; }

    public Integer getBookedCount() { return bookedCount; }
    public void setBookedCount(Integer bookedCount) { this.bookedCount = bookedCount; }

    public List<SeatStatusDto> getSeats() { return seats; }
    public void setSeats(List<SeatStatusDto> seats) { this.seats = seats; }
}
