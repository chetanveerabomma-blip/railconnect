package com.railconnect.seatengine.service;

import com.railconnect.seatengine.dto.SeatLockResponse;
import com.railconnect.seatengine.model.Seat;
import com.railconnect.seatengine.model.SeatLock;
import com.railconnect.seatengine.model.SeatState;
import com.railconnect.seatengine.repository.SeatRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SeatLockService {

    // Dynamic thread-safe in-memory seat lock table
    private final Map<Long, SeatLock> lockMap = new ConcurrentHashMap<>();
    private final SeatRepository seatRepository;

    public SeatLockService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public synchronized SeatLockResponse lockSeat(Long seatId, Long userId, Long durationMinutes) {
        long duration = (durationMinutes != null && durationMinutes > 0) ? durationMinutes : 10L;

        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found with ID: " + seatId));

        SeatLock currentLock = lockMap.get(seatId);
        if (currentLock != null && !currentLock.isExpired()) {
            if (!currentLock.getUserId().equals(userId)) {
                throw new IllegalStateException("Seat " + seat.getSeatNumber() + " is temporarily locked by another passenger. Please select another seat.");
            }
            // Same user renewing lock
            currentLock.setExpiryTime(java.time.Instant.now().plusSeconds(duration * 60));
            return new SeatLockResponse(seatId, seat.getCoach().getCoachNumber(), seat.getSeatNumber(), true, currentLock.getRemainingSeconds(), "Seat lock renewed for 10 minutes.");
        }

        SeatLock newLock = new SeatLock(seatId, userId, duration);
        lockMap.put(seatId, newLock);

        return new SeatLockResponse(
                seatId,
                seat.getCoach().getCoachNumber(),
                seat.getSeatNumber(),
                true,
                newLock.getRemainingSeconds(),
                "Seat successfully locked for 10 minutes. Please complete payment."
        );
    }

    public synchronized boolean unlockSeat(Long seatId, Long userId) {
        SeatLock currentLock = lockMap.get(seatId);
        if (currentLock != null) {
            if (userId == null || currentLock.getUserId().equals(userId)) {
                lockMap.remove(seatId);
                return true;
            }
        }
        return false;
    }

    public synchronized void confirmSeat(Long seatId) {
        // Seat transition from LOCKED -> BOOKED
        lockMap.remove(seatId);
    }

    public boolean isSeatLocked(Long seatId) {
        SeatLock currentLock = lockMap.get(seatId);
        if (currentLock == null) {
            return false;
        }
        if (currentLock.isExpired()) {
            lockMap.remove(seatId);
            return false;
        }
        return true;
    }

    public Long getRemainingSeconds(Long seatId) {
        SeatLock lock = lockMap.get(seatId);
        if (lock != null && !lock.isExpired()) {
            return lock.getRemainingSeconds();
        }
        return 0L;
    }

    public SeatState getSeatLockState(Long seatId, Long userId) {
        SeatLock lock = lockMap.get(seatId);
        if (lock != null && !lock.isExpired()) {
            if (userId != null && lock.getUserId().equals(userId)) {
                return SeatState.SELECTED;
            }
            return SeatState.LOCKED;
        }
        return SeatState.AVAILABLE;
    }

    // Background eviction for expired locks every 30 seconds
    @Scheduled(fixedRate = 30000)
    public void cleanupExpiredLocks() {
        lockMap.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    public void clearAllLocks() {
        lockMap.clear();
    }
}
