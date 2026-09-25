package com.railconnect.seatengine.service;

import com.railconnect.seatengine.dto.CoachLayoutDto;
import com.railconnect.seatengine.dto.SeatStatusDto;
import com.railconnect.seatengine.model.Coach;
import com.railconnect.seatengine.model.Seat;
import com.railconnect.seatengine.model.SeatState;
import com.railconnect.seatengine.repository.CoachRepository;
import com.railconnect.seatengine.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CoachService {

    private final CoachRepository coachRepository;
    private final SeatRepository seatRepository;
    private final SeatLockService seatLockService;

    public CoachService(CoachRepository coachRepository,
                        SeatRepository seatRepository,
                        SeatLockService seatLockService) {
        this.coachRepository = coachRepository;
        this.seatRepository = seatRepository;
        this.seatLockService = seatLockService;
    }

    public List<Coach> getCoachesByTrain(Long trainId) {
        return coachRepository.findByTrainId(trainId);
    }

    public CoachLayoutDto getCoachLayout(Long coachId, Set<Long> bookedSeatIds, Long currentUserId) {
        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new IllegalArgumentException("Coach not found with ID: " + coachId));

        List<Seat> seats = seatRepository.findByCoachId(coachId);

        CoachLayoutDto layout = new CoachLayoutDto();
        layout.setCoachId(coach.getId());
        layout.setTrainId(coach.getTrainId());
        layout.setCoachNumber(coach.getCoachNumber());
        layout.setCoachType(coach.getCoachType());
        layout.setTotalSeats(coach.getTotalSeats());

        int available = 0;
        int locked = 0;
        int booked = 0;

        List<SeatStatusDto> seatDtos = new ArrayList<>();

        for (Seat s : seats) {
            SeatState state;
            long remainingSeconds = 0;

            if (bookedSeatIds != null && bookedSeatIds.contains(s.getId())) {
                state = SeatState.BOOKED;
                booked++;
            } else if (seatLockService.isSeatLocked(s.getId())) {
                state = seatLockService.getSeatLockState(s.getId(), currentUserId);
                locked++;
                remainingSeconds = seatLockService.getRemainingSeconds(s.getId());
            } else {
                state = SeatState.AVAILABLE;
                available++;
            }

            SeatStatusDto dto = new SeatStatusDto(
                    s.getId(),
                    coach.getId(),
                    coach.getCoachNumber(),
                    s.getSeatNumber(),
                    s.getBerthType(),
                    s.getCabinNumber(),
                    state,
                    remainingSeconds
            );
            seatDtos.add(dto);
        }

        layout.setSeats(seatDtos);
        layout.setAvailableCount(available);
        layout.setLockedCount(locked);
        layout.setBookedCount(booked);

        return layout;
    }
}
