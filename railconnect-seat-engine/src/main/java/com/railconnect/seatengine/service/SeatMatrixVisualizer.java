package com.railconnect.seatengine.service;

import com.railconnect.seatengine.model.Coach;
import com.railconnect.seatengine.model.Seat;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Visual matrix serializer and ASCII coach layout mapper.
 * Generates structured 2D berth matrices for terminal diagnostics and rich UI renderers.
 */
@Service
public class SeatMatrixVisualizer {

    public static class SeatGridCell {
        private final int seatNumber;
        private final String berthType;
        private final int cabin;
        private final String status; // AVAILABLE, LOCKED, BOOKED

        public SeatGridCell(int seatNumber, String berthType, int cabin, String status) {
            this.seatNumber = seatNumber;
            this.berthType = berthType;
            this.cabin = cabin;
            this.status = status;
        }

        public int getSeatNumber() { return seatNumber; }
        public String getBerthType() { return berthType; }
        public int getCabin() { return cabin; }
        public String getStatus() { return status; }
    }

    public static class CoachMatrixView {
        private final String coachNumber;
        private final String coachType;
        private final int totalCapacity;
        private final List<List<SeatGridCell>> cabinRows;

        public CoachMatrixView(String coachNumber, String coachType, int totalCapacity, List<List<SeatGridCell>> cabinRows) {
            this.coachNumber = coachNumber;
            this.coachType = coachType;
            this.totalCapacity = totalCapacity;
            this.cabinRows = cabinRows;
        }

        public String getCoachNumber() { return coachNumber; }
        public String getCoachType() { return coachType; }
        public int getTotalCapacity() { return totalCapacity; }
        public List<List<SeatGridCell>> getCabinRows() { return cabinRows; }
    }

    public CoachMatrixView generateMatrix(Coach coach, List<Seat> allSeats, Set<Long> bookedSeatIds, Set<Long> lockedSeatIds) {
        Map<Integer, List<SeatGridCell>> byCabin = new TreeMap<>();

        for (Seat s : allSeats) {
            String status = "AVAILABLE";
            if (bookedSeatIds != null && bookedSeatIds.contains(s.getId())) {
                status = "BOOKED";
            } else if (lockedSeatIds != null && lockedSeatIds.contains(s.getId())) {
                status = "LOCKED";
            }
            SeatGridCell cell = new SeatGridCell(s.getSeatNumber(), s.getBerthType().name(), s.getCabinNumber(), status);
            byCabin.computeIfAbsent(s.getCabinNumber(), k -> new ArrayList<>()).add(cell);
        }

        List<List<SeatGridCell>> rows = new ArrayList<>(byCabin.values());
        return new CoachMatrixView(coach.getCoachNumber(), coach.getCoachType(), coach.getTotalSeats(), rows);
    }
}
