package com.railconnect.seatengine.dto;

import com.railconnect.seatengine.model.BerthType;
import java.util.ArrayList;
import java.util.List;

public class AutoAllocateResponse {
    private Long trainId;
    private String coachType;
    private boolean successful;
    private String message;
    private List<AllocationItem> allocations = new ArrayList<>();

    public AutoAllocateResponse() {}

    public static class AllocationItem {
        private String passengerName;
        private Long seatId;
        private String coachNumber;
        private Integer seatNumber;
        private BerthType berthType;
        private Integer cabinNumber;

        public AllocationItem() {}

        public AllocationItem(String passengerName, Long seatId, String coachNumber, Integer seatNumber, BerthType berthType, Integer cabinNumber) {
            this.passengerName = passengerName;
            this.seatId = seatId;
            this.coachNumber = coachNumber;
            this.seatNumber = seatNumber;
            this.berthType = berthType;
            this.cabinNumber = cabinNumber;
        }

        public String getPassengerName() { return passengerName; }
        public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

        public Long getSeatId() { return seatId; }
        public void setSeatId(Long seatId) { this.seatId = seatId; }

        public String getCoachNumber() { return coachNumber; }
        public void setCoachNumber(String coachNumber) { this.coachNumber = coachNumber; }

        public Integer getSeatNumber() { return seatNumber; }
        public void setSeatNumber(Integer seatNumber) { this.seatNumber = seatNumber; }

        public BerthType getBerthType() { return berthType; }
        public void setBerthType(BerthType berthType) { this.berthType = berthType; }

        public Integer getCabinNumber() { return cabinNumber; }
        public void setCabinNumber(Integer cabinNumber) { this.cabinNumber = cabinNumber; }
    }

    public Long getTrainId() { return trainId; }
    public void setTrainId(Long trainId) { this.trainId = trainId; }

    public String getCoachType() { return coachType; }
    public void setCoachType(String coachType) { this.coachType = coachType; }

    public boolean isSuccessful() { return successful; }
    public void setSuccessful(boolean successful) { this.successful = successful; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<AllocationItem> getAllocations() { return allocations; }
    public void setAllocations(List<AllocationItem> allocations) { this.allocations = allocations; }
}
