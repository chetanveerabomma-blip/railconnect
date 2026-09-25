package com.railconnect.seatengine;

import com.railconnect.seatengine.dto.AutoAllocateRequest;
import com.railconnect.seatengine.dto.AutoAllocateResponse;
import com.railconnect.seatengine.dto.SeatLockResponse;
import com.railconnect.seatengine.model.BerthType;
import com.railconnect.seatengine.model.Coach;
import com.railconnect.seatengine.model.Seat;
import com.railconnect.seatengine.repository.SeatRepository;
import com.railconnect.seatengine.service.SeatAllocationService;
import com.railconnect.seatengine.service.SeatLockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SeatEngineTest {

    private SeatRepository seatRepository;
    private SeatLockService seatLockService;
    private SeatAllocationService seatAllocationService;

    @BeforeEach
    void setUp() {
        seatRepository = mock(SeatRepository.class);
        seatLockService = new SeatLockService(seatRepository);
        seatAllocationService = new SeatAllocationService(seatRepository, seatLockService);
    }

    @Test
    void testDynamicSeatLockingAndExclusion() {
        Coach coach = new Coach(1L, "B1", "3A", 72);
        Seat seat1 = new Seat(coach, 1, BerthType.LOWER, 1);
        seat1.setId(101L);

        when(seatRepository.findById(101L)).thenReturn(Optional.of(seat1));

        // User A locks seat 101
        SeatLockResponse respA = seatLockService.lockSeat(101L, 1L, 10L);
        assertTrue(respA.isLocked());
        assertTrue(respA.getRemainingSeconds() > 0);

        // User B tries to lock the same seat -> Should throw IllegalStateException (conflict)
        assertThrows(IllegalStateException.class, () -> {
            seatLockService.lockSeat(101L, 2L, 10L);
        });

        // User A unlocks seat 101
        boolean unlocked = seatLockService.unlockSeat(101L, 1L);
        assertTrue(unlocked);

        // Now User B can lock it
        SeatLockResponse respB = seatLockService.lockSeat(101L, 2L, 10L);
        assertTrue(respB.isLocked());
    }

    @Test
    void testIntelligentAutoAllocationPrefersLowerBerthForSeniorCitizen() {
        Coach coach = new Coach(1L, "B1", "3A", 72);
        coach.setId(1L);

        Seat seat1 = new Seat(coach, 1, BerthType.LOWER, 1);
        seat1.setId(1L);
        Seat seat2 = new Seat(coach, 2, BerthType.MIDDLE, 1);
        seat2.setId(2L);
        Seat seat3 = new Seat(coach, 3, BerthType.UPPER, 1);
        seat3.setId(3L);

        List<Seat> seats = List.of(seat1, seat2, seat3);
        when(seatRepository.findByTrainIdAndCoachType(1L, "3A")).thenReturn(seats);
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat1));
        when(seatRepository.findById(2L)).thenReturn(Optional.of(seat2));
        when(seatRepository.findById(3L)).thenReturn(Optional.of(seat3));

        AutoAllocateRequest request = new AutoAllocateRequest();
        request.setTrainId(1L);
        request.setCoachType("3A");

        // Senior citizen passenger (age 68) with no preference specified -> Should receive LOWER berth
        AutoAllocateRequest.PassengerPreference senior = new AutoAllocateRequest.PassengerPreference("Grandpa", 68, "MALE", "NO_PREFERENCE");
        request.setPassengers(List.of(senior));

        AutoAllocateResponse response = seatAllocationService.autoAllocateSeats(request, Collections.emptySet(), 10L);

        assertTrue(response.isSuccessful());
        assertEquals(1, response.getAllocations().size());
        assertEquals(BerthType.LOWER, response.getAllocations().get(0).getBerthType());
        assertEquals(1, response.getAllocations().get(0).getSeatNumber());
    }
}
