package com.railconnect.berthexchange.controller;

import com.railconnect.auth.jwt.JwtUtil;
import com.railconnect.berthexchange.dto.ExchangeInitiateRequest;
import com.railconnect.berthexchange.dto.ExchangeRequestDto;
import com.railconnect.berthexchange.service.BerthExchangeService;
import com.railconnect.booking.model.BookingPassenger;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/berth-exchange")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BerthExchangeController {

    private final BerthExchangeService berthExchangeService;
    private final JwtUtil jwtUtil;

    public BerthExchangeController(BerthExchangeService berthExchangeService, JwtUtil jwtUtil) {
        this.berthExchangeService = berthExchangeService;
        this.jwtUtil = jwtUtil;
    }

    private Long getUserId(Authentication authentication, String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                Long id = jwtUtil.extractUserId(authHeader.substring(7));
                if (id != null) return id;
            } catch (Exception ignored) {}
        }
        return 3L; // Default passenger id (Rahul Sharma)
    }

    @PostMapping("/request")
    public ResponseEntity<?> initiateExchange(
            @Valid @RequestBody ExchangeInitiateRequest request,
            Authentication authentication,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            Long userId = getUserId(authentication, authHeader);
            ExchangeRequestDto dto = berthExchangeService.initiateExchange(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to initiate exchange: " + e.getMessage()));
        }
    }

    @PostMapping("/respond/{id}")
    public ResponseEntity<?> respondToRequest(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            Authentication authentication,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            Long userId = getUserId(authentication, authHeader);
            boolean accept = Boolean.TRUE.equals(body.get("accept"));
            String reason = (String) body.get("reason");
            ExchangeRequestDto dto = berthExchangeService.respondToRequest(id, accept, reason, userId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<List<ExchangeRequestDto>> getMyRequests(
            Authentication authentication,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = getUserId(authentication, authHeader);
        List<ExchangeRequestDto> list = berthExchangeService.getRequestsByUser(userId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/eligible")
    public ResponseEntity<List<BookingPassenger>> getEligiblePassengers(
            @RequestParam Long trainId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate journeyDate,
            @RequestParam String coachType,
            @RequestParam(required = false) Long excludeBookingId) {
        List<BookingPassenger> passengers = berthExchangeService.findEligiblePassengersForExchange(
                trainId, journeyDate, coachType, excludeBookingId
        );
        return ResponseEntity.ok(passengers);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ExchangeRequestDto>> getAllRequests() {
        return ResponseEntity.ok(berthExchangeService.getAllRequests());
    }

    @PostMapping("/admin/review/{id}")
    public ResponseEntity<?> adminReview(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            Authentication authentication,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            Long adminId = getUserId(authentication, authHeader);
            boolean approve = Boolean.TRUE.equals(body.get("approve"));
            String notes = (String) body.get("adminNotes");
            ExchangeRequestDto dto = berthExchangeService.adminApprove(id, approve, notes, adminId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
