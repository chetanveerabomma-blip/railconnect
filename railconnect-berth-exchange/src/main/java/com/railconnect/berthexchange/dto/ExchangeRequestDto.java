package com.railconnect.berthexchange.dto;

import com.railconnect.berthexchange.model.ExchangeStatus;

public class ExchangeRequestDto {
    private Long id;
    private Long requesterBookingId;
    private String requesterPnr;
    private String requesterPassengerName;
    private String requesterCoach;
    private Integer requesterSeatNumber;
    private String requesterBerthType;

    private Long targetBookingId;
    private String targetPnr;
    private String targetPassengerName;
    private String targetCoach;
    private Integer targetSeatNumber;
    private String targetBerthType;

    private String trainNumber;
    private String trainName;
    private String journeyDate;
    private ExchangeStatus status;
    private String requestReason;
    private String adminNotes;
    private String createdAt;

    public ExchangeRequestDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRequesterBookingId() { return requesterBookingId; }
    public void setRequesterBookingId(Long requesterBookingId) { this.requesterBookingId = requesterBookingId; }

    public String getRequesterPnr() { return requesterPnr; }
    public void setRequesterPnr(String requesterPnr) { this.requesterPnr = requesterPnr; }

    public String getRequesterPassengerName() { return requesterPassengerName; }
    public void setRequesterPassengerName(String requesterPassengerName) { this.requesterPassengerName = requesterPassengerName; }

    public String getRequesterCoach() { return requesterCoach; }
    public void setRequesterCoach(String requesterCoach) { this.requesterCoach = requesterCoach; }

    public Integer getRequesterSeatNumber() { return requesterSeatNumber; }
    public void setRequesterSeatNumber(Integer requesterSeatNumber) { this.requesterSeatNumber = requesterSeatNumber; }

    public String getRequesterBerthType() { return requesterBerthType; }
    public void setRequesterBerthType(String requesterBerthType) { this.requesterBerthType = requesterBerthType; }

    public Long getTargetBookingId() { return targetBookingId; }
    public void setTargetBookingId(Long targetBookingId) { this.targetBookingId = targetBookingId; }

    public String getTargetPnr() { return targetPnr; }
    public void setTargetPnr(String targetPnr) { this.targetPnr = targetPnr; }

    public String getTargetPassengerName() { return targetPassengerName; }
    public void setTargetPassengerName(String targetPassengerName) { this.targetPassengerName = targetPassengerName; }

    public String getTargetCoach() { return targetCoach; }
    public void setTargetCoach(String targetCoach) { this.targetCoach = targetCoach; }

    public Integer getTargetSeatNumber() { return targetSeatNumber; }
    public void setTargetSeatNumber(Integer targetSeatNumber) { this.targetSeatNumber = targetSeatNumber; }

    public String getTargetBerthType() { return targetBerthType; }
    public void setTargetBerthType(String targetBerthType) { this.targetBerthType = targetBerthType; }

    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }

    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }

    public String getJourneyDate() { return journeyDate; }
    public void setJourneyDate(String journeyDate) { this.journeyDate = journeyDate; }

    public ExchangeStatus getStatus() { return status; }
    public void setStatus(ExchangeStatus status) { this.status = status; }

    public String getRequestReason() { return requestReason; }
    public void setRequestReason(String requestReason) { this.requestReason = requestReason; }

    public String getAdminNotes() { return adminNotes; }
    public void setAdminNotes(String adminNotes) { this.adminNotes = adminNotes; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
