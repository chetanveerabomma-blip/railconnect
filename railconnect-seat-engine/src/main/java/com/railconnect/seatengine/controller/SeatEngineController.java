package com.railconnect.seatengine.controller;

import com.railconnect.auth.jwt.JwtUtil;
import com.railconnect.seatengine.dto.*;
import com.railconnect.seatengine.model.Coach;
import com.railconnect.seatengine.service.CoachService;
import com.railconnect.seatengine.service.SeatAllocationService;
import com.railconnect.seatengine.service.SeatLockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SeatEngineController {

    private final CoachService coachService;
    private final SeatLockService seatLockService;
    private final SeatAllocationService seatAllocationService;
    private final JwtUtil jwtUtil;

    public SeatEngineController(CoachService coachService,
                                SeatLockService seatLockService,
                                SeatAllocationService seatAllocationService,
                                JwtUtil jwtUtil) {
        this.coachService = coachService;
        this.seatLockService = seatLockService;
        this.seatAllocationService = seatAllocationService;
        this.jwtUtil = jwtUtil;
    }

    private Long extractUserId(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                return jwtUtil.extractUserId(token);
            } catch (Exception ignored) {}
        }
        return 1L; // fallback guest / default user id
    }

    @GetMapping("/coaches/{trainId}")
    public ResponseEntity<List<Coach>> getCoaches(@PathVariable Long trainId) {
        return ResponseEntity.ok(coachService.getCoachesByTrain(trainId));
    }

    @GetMapping("/layout/{coachId}")
    public ResponseEntity<CoachLayoutDto> getCoachLayout(
            @PathVariable Long coachId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = extractUserId(authHeader);
        CoachLayoutDto layout = coachService.getCoachLayout(coachId, Collections.emptySet(), userId);
        return ResponseEntity.ok(layout);
    }

    @PostMapping("/lock")
    public ResponseEntity<?> lockSeat(
            @Valid @RequestBody SeatLockRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = extractUserId(authHeader);
        try {
            SeatLockResponse response = seatLockService.lockSeat(request.getSeatId(), userId, request.getDurationMinutes());
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/unlock")
    public ResponseEntity<?> unlockSeat(
            @RequestBody Map<String, Long> payload,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long seatId = payload.get("seatId");
        Long userId = extractUserId(authHeader);
        if (seatId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "seatId is required"));
        }
        boolean unlocked = seatLockService.unlockSeat(seatId, userId);
        return ResponseEntity.ok(Map.of("seatId", seatId, "unlocked", unlocked));
    }

    @PostMapping("/auto-allocate")
    public ResponseEntity<?> autoAllocate(
            @RequestBody AutoAllocateRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = extractUserId(authHeader);
        AutoAllocateResponse response = seatAllocationService.autoAllocateSeats(request, Collections.emptySet(), userId);
        if (!response.isSuccessful()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
