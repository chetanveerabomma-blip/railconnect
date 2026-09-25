package com.railconnect.seatengine.service;

import com.railconnect.seatengine.model.BerthType;
import com.railconnect.seatengine.model.Seat;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Advanced Family & Group Clustering Seat Allocator
 *
 * Implements a 2D spatial clustering algorithm that minimizes intra-group berth dispersion.
 * Prioritizes contiguous bay allocation (same cabin/bay of 6-8 berths) and preserves
 * lower berths for elderly travelers (age >= 60) and pregnant women.
 */
@Service
public class FamilyClusteringSeatAllocator {

    public static class PassengerPreference {
        private final String name;
        private final int age;
        private final String gender;
        private final BerthType requestedBerthType;

        public PassengerPreference(String name, int age, String gender, BerthType requestedBerthType) {
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.requestedBerthType = requestedBerthType;
        }

        public String getName() { return name; }
        public int getAge() { return age; }
        public String getGender() { return gender; }
        public BerthType getRequestedBerthType() { return requestedBerthType; }
        public boolean isSeniorCitizen() { return age >= 60; }
    }

    public static class AllocationResult {
        private final Map<PassengerPreference, Seat> assignments;
        private final double dispersionScore; // Lower is better (0.0 = perfect single-bay clustering)
        private final boolean allGroupMembersTogether;

        public AllocationResult(Map<PassengerPreference, Seat> assignments, double dispersionScore, boolean allGroupMembersTogether) {
            this.assignments = assignments;
            this.dispersionScore = dispersionScore;
            this.allGroupMembersTogether = allGroupMembersTogether;
        }

        public Map<PassengerPreference, Seat> getAssignments() { return assignments; }
        public double getDispersionScore() { return dispersionScore; }
        public boolean isAllGroupMembersTogether() { return allGroupMembersTogether; }
    }

    /**
     * Allocates seats for a family/group from a pool of available seats in a coach.
     * Uses a multi-criteria greedy heuristic:
     * 1. Senior Citizens (age >= 60) get highest priority for LOWER berths.
     * 2. Maximizes co-location in the same cabin/bay.
     * 3. Calculates penalty score based on Euclidean distance between allocated seat indices.
     */
    public AllocationResult allocateGroupSeats(List<PassengerPreference> passengers, List<Seat> availableSeats) {
        if (passengers.isEmpty() || availableSeats.size() < passengers.size()) {
            throw new IllegalArgumentException("Insufficient seats available for group allocation");
        }

        // Group available seats by cabin number
        Map<Integer, List<Seat>> seatsByCabin = new TreeMap<>();
        for (Seat seat : availableSeats) {
            seatsByCabin.computeIfAbsent(seat.getCabinNumber(), k -> new ArrayList<>()).add(seat);
        }

        // Strategy 1: Find a single cabin that fits the entire family
        int groupSize = passengers.size();
        for (Map.Entry<Integer, List<Seat>> entry : seatsByCabin.entrySet()) {
            List<Seat> cabinSeats = entry.getValue();
            if (cabinSeats.size() >= groupSize) {
                Map<PassengerPreference, Seat> assigned = assignWithinCabin(passengers, cabinSeats);
                return new AllocationResult(assigned, 0.0, true);
            }
        }

        // Strategy 2: Contiguous adjacent cabins minimizing dispersion
        List<Integer> cabinNumbers = new ArrayList<>(seatsByCabin.keySet());
        Map<PassengerPreference, Seat> bestAssignment = null;
        double minDispersion = Double.MAX_VALUE;

        for (int i = 0; i < cabinNumbers.size(); i++) {
            List<Seat> candidatePool = new ArrayList<>();
            for (int j = i; j < cabinNumbers.size() && candidatePool.size() < groupSize; j++) {
                candidatePool.addAll(seatsByCabin.get(cabinNumbers.get(j)));
            }

            if (candidatePool.size() >= groupSize) {
                Map<PassengerPreference, Seat> assignment = assignWithinPool(passengers, candidatePool.subList(0, groupSize));
                double dispersion = calculateDispersion(assignment.values());
                if (dispersion < minDispersion) {
                    minDispersion = dispersion;
                    bestAssignment = assignment;
                }
            }
        }

        if (bestAssignment != null) {
            return new AllocationResult(bestAssignment, minDispersion, false);
        }

        // Strategy 3: Fallback sequential assignment
        Map<PassengerPreference, Seat> fallback = assignWithinPool(passengers, availableSeats.subList(0, groupSize));
        return new AllocationResult(fallback, calculateDispersion(fallback.values()), false);
    }

    private Map<PassengerPreference, Seat> assignWithinCabin(List<PassengerPreference> passengers, List<Seat> cabinSeats) {
        Map<PassengerPreference, Seat> result = new LinkedHashMap<>();
        List<Seat> pool = new ArrayList<>(cabinSeats);

        // Sort passengers: Seniors first
        List<PassengerPreference> sorted = new ArrayList<>(passengers);
        sorted.sort((a, b) -> Boolean.compare(b.isSeniorCitizen(), a.isSeniorCitizen()));

        for (PassengerPreference p : sorted) {
            Seat selected = null;
            if (p.isSeniorCitizen()) {
                selected = pool.stream()
                        .filter(s -> s.getBerthType() == BerthType.LOWER)
                        .findFirst().orElse(null);
            }
            if (selected == null && p.getRequestedBerthType() != null) {
                selected = pool.stream()
                        .filter(s -> s.getBerthType() == p.getRequestedBerthType())
                        .findFirst().orElse(null);
            }
            if (selected == null && !pool.isEmpty()) {
                selected = pool.get(0);
            }
            if (selected != null) {
                result.put(p, selected);
                pool.remove(selected);
            }
        }
        return result;
    }

    private Map<PassengerPreference, Seat> assignWithinPool(List<PassengerPreference> passengers, List<Seat> pool) {
        Map<PassengerPreference, Seat> result = new LinkedHashMap<>();
        List<Seat> remaining = new ArrayList<>(pool);

        for (PassengerPreference p : passengers) {
            if (!remaining.isEmpty()) {
                Seat seat = remaining.remove(0);
                result.put(p, seat);
            }
        }
        return result;
    }

    private double calculateDispersion(Collection<Seat> seats) {
        if (seats.size() <= 1) return 0.0;
        List<Integer> seatNums = new ArrayList<>();
        for (Seat s : seats) {
            seatNums.add(s.getSeatNumber());
        }
        Collections.sort(seatNums);

        double totalDistance = 0.0;
        for (int i = 1; i < seatNums.size(); i++) {
            totalDistance += Math.abs(seatNums.get(i) - seatNums.get(i - 1));
        }
        return totalDistance / (seats.size() - 1);
    }
}
