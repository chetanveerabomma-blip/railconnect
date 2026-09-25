package com.railconnect.seatengine.service;

import com.railconnect.seatengine.dto.AutoAllocateRequest;
import com.railconnect.seatengine.dto.AutoAllocateResponse;
import com.railconnect.seatengine.model.BerthType;
import com.railconnect.seatengine.model.Seat;
import com.railconnect.seatengine.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SeatAllocationService {

    private final SeatRepository seatRepository;
    private final SeatLockService seatLockService;

    public SeatAllocationService(SeatRepository seatRepository, SeatLockService seatLockService) {
        this.seatRepository = seatRepository;
        this.seatLockService = seatLockService;
    }

    /**
     * Intelligent Seat Allocation Algorithm using DSA structures:
     * - Queue (FIFO passenger queue)
     * - HashMap (Coach-wise seat grouping)
     * - HashSet (O(1) booked and selected seat tracking)
     * - PriorityQueue (Optimal berth preference scoring & cabin proximity)
     * - ArrayList (Allocation result collection)
     */
    public AutoAllocateResponse autoAllocateSeats(AutoAllocateRequest request, Set<Long> bookedSeatIds, Long currentUserId) {
        AutoAllocateResponse response = new AutoAllocateResponse();
        response.setTrainId(request.getTrainId());
        response.setCoachType(request.getCoachType());

        if (request.getPassengers() == null || request.getPassengers().isEmpty()) {
            response.setSuccessful(false);
            response.setMessage("No passenger preferences provided for auto-allocation.");
            return response;
        }

        // 1. Fetch candidate seats in train for selected class
        List<Seat> allSeats = seatRepository.findByTrainIdAndCoachType(request.getTrainId(), request.getCoachType());

        // 2. Filter out already booked and currently locked seats (HashSet for O(1) lookup)
        Set<Long> unavailableSeatIds = new HashSet<>();
        if (bookedSeatIds != null) {
            unavailableSeatIds.addAll(bookedSeatIds);
        }
        for (Seat s : allSeats) {
            if (seatLockService.isSeatLocked(s.getId())) {
                unavailableSeatIds.add(s.getId());
            }
        }

        // 3. Group available seats by Coach using HashMap
        Map<String, List<Seat>> coachSeatMap = new HashMap<>();
        for (Seat s : allSeats) {
            if (!unavailableSeatIds.contains(s.getId())) {
                coachSeatMap.computeIfAbsent(s.getCoach().getCoachNumber(), k -> new ArrayList<>()).add(s);
            }
        }

        int totalPassengers = request.getPassengers().size();

        // 4. Select the best Coach that can fit the entire group (Group clustering)
        String targetCoachNumber = null;
        int maxAvailableInSingleCoach = 0;
        for (Map.Entry<String, List<Seat>> entry : coachSeatMap.entrySet()) {
            int count = entry.getValue().size();
            if (count >= totalPassengers) {
                targetCoachNumber = entry.getKey();
                break;
            }
            if (count > maxAvailableInSingleCoach) {
                maxAvailableInSingleCoach = count;
                targetCoachNumber = entry.getKey();
            }
        }

        if (targetCoachNumber == null || coachSeatMap.isEmpty()) {
            response.setSuccessful(false);
            response.setMessage("Insufficient available seats for auto-allocation in class " + request.getCoachType());
            return response;
        }

        // 5. FIFO Queue for passengers to allocate
        Queue<AutoAllocateRequest.PassengerPreference> passengerQueue = new LinkedList<>(request.getPassengers());

        // Track seats allocated during this allocation session
        Set<Long> batchAllocatedSeatIds = new HashSet<>();
        List<AutoAllocateResponse.AllocationItem> allocationItems = new ArrayList<>();

        // Keep track of the dominant cabin for family adjacency
        Integer preferredCabin = null;

        while (!passengerQueue.isEmpty()) {
            AutoAllocateRequest.PassengerPreference passenger = passengerQueue.poll();
            String requestedPref = passenger.getPreference();

            // Special requirement: Senior citizens (age >= 60) get LOWER berth priority
            if (passenger.isSeniorCitizen() && (requestedPref == null || "NO_PREFERENCE".equalsIgnoreCase(requestedPref))) {
                requestedPref = "LOWER";
            }

            final String effectivePref = requestedPref;
            final Integer currentCabinTarget = preferredCabin;

            // PriorityQueue for scoring seats: higher score wins
            PriorityQueue<SeatCandidate> candidateQueue = new PriorityQueue<>(
                    (a, b) -> Integer.compare(b.score, a.score)
            );

            // Search in target coach first, then other coaches if needed
            List<Seat> pool = coachSeatMap.getOrDefault(targetCoachNumber, Collections.emptyList());
            if (pool.stream().noneMatch(s -> !batchAllocatedSeatIds.contains(s.getId()))) {
                // Coach filled up, fallback to any available coach
                for (List<Seat> otherCoachSeats : coachSeatMap.values()) {
                    if (otherCoachSeats.stream().anyMatch(s -> !batchAllocatedSeatIds.contains(s.getId()))) {
                        pool = otherCoachSeats;
                        break;
                    }
                }
            }

            for (Seat seat : pool) {
                if (batchAllocatedSeatIds.contains(seat.getId()) || unavailableSeatIds.contains(seat.getId())) {
                    continue;
                }

                int score = 0;

                // Preference Match scoring
                if (effectivePref != null && !effectivePref.isBlank() && !"NO_PREFERENCE".equalsIgnoreCase(effectivePref)) {
                    if (seat.getBerthType().name().equalsIgnoreCase(effectivePref)) {
                        score += 50;
                    }
                }

                // Family & Group Proximity: Adjacent cabin matching
                if (currentCabinTarget != null) {
                    if (Objects.equals(seat.getCabinNumber(), currentCabinTarget)) {
                        score += 30; // Same cabin
                    } else if (Math.abs(seat.getCabinNumber() - currentCabinTarget) == 1) {
                        score += 15; // Adjacent cabin
                    }
                }

                // Senior citizen lower berth bonus
                if (passenger.isSeniorCitizen() && seat.getBerthType() == BerthType.LOWER) {
                    score += 40;
                }

                candidateQueue.offer(new SeatCandidate(seat, score));
            }

            if (candidateQueue.isEmpty()) {
                response.setSuccessful(false);
                response.setMessage("Could not find remaining valid seats for passenger: " + passenger.getName());
                return response;
            }

            Seat bestSeat = candidateQueue.poll().seat;
            batchAllocatedSeatIds.add(bestSeat.getId());
            preferredCabin = bestSeat.getCabinNumber();

            // Lock seat for the user
            if (currentUserId != null) {
                seatLockService.lockSeat(bestSeat.getId(), currentUserId, 10L);
            }

            allocationItems.add(new AutoAllocateResponse.AllocationItem(
                    passenger.getName(),
                    bestSeat.getId(),
                    bestSeat.getCoach().getCoachNumber(),
                    bestSeat.getSeatNumber(),
                    bestSeat.getBerthType(),
                    bestSeat.getCabinNumber()
            ));
        }

        response.setSuccessful(true);
        response.setAllocations(allocationItems);
        response.setMessage("Successfully allocated " + allocationItems.size() + " seats using intelligent proximity & preference algorithm.");
        return response;
    }

    private static class SeatCandidate {
        Seat seat;
        int score;

        SeatCandidate(Seat seat, int score) {
            this.seat = seat;
            this.score = score;
        }
    }
}
