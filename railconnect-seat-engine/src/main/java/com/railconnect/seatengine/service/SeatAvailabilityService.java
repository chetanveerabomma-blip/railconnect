package com.railconnect.seatengine.service;

import com.railconnect.seatengine.model.Seat;
import com.railconnect.seatengine.repository.CoachRepository;
import com.railconnect.seatengine.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SeatAvailabilityService {

    private final SeatRepository seatRepository;
    private final CoachRepository coachRepository;
    private final SeatLockService seatLockService;

    public SeatAvailabilityService(SeatRepository seatRepository,
                                   CoachRepository coachRepository,
                                   SeatLockService seatLockService) {
        this.seatRepository = seatRepository;
        this.coachRepository = coachRepository;
        this.seatLockService = seatLockService;
    }

    public int getAvailableSeatsCount(Long trainId, String coachType, Set<Long> bookedSeatIds) {
        List<Seat> seats = seatRepository.findByTrainIdAndCoachType(trainId, coachType);
        int available = 0;
        for (Seat s : seats) {
            boolean isBooked = (bookedSeatIds != null && bookedSeatIds.contains(s.getId()));
            boolean isLocked = seatLockService.isSeatLocked(s.getId());
            if (!isBooked && !isLocked) {
                available++;
            }
        }
        return available;
    }

    public List<Seat> getAvailableSeatsList(Long trainId, String coachType, Set<Long> bookedSeatIds) {
        List<Seat> seats = seatRepository.findByTrainIdAndCoachType(trainId, coachType);
        return seats.stream()
                .filter(s -> (bookedSeatIds == null || !bookedSeatIds.contains(s.getId())))
                .filter(s -> !seatLockService.isSeatLocked(s.getId()))
                .collect(Collectors.toList());
    }
}
