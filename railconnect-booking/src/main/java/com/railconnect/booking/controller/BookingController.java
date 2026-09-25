package com.railconnect.booking.controller;

import com.railconnect.auth.jwt.JwtUtil;
import com.railconnect.booking.dto.BookingRequest;
import com.railconnect.booking.dto.BookingResponse;
import com.railconnect.booking.model.Booking;
import com.railconnect.booking.service.BookingService;
import com.railconnect.booking.service.CancellationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BookingController {

    private final BookingService bookingService;
    private final CancellationService cancellationService;
    private final JwtUtil jwtUtil;

    public BookingController(BookingService bookingService,
                             CancellationService cancellationService,
                             JwtUtil jwtUtil) {
        this.bookingService = bookingService;
        this.cancellationService = cancellationService;
        this.jwtUtil = jwtUtil;
    }

    private String getUsername(Authentication authentication, String authHeader) {
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            return authentication.getName();
        }
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                return jwtUtil.extractUsername(authHeader.substring(7));
            } catch (Exception ignored) {}
        }
        return "rahul_sharma"; // Default demo passenger fallback
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String username = getUsername(authentication, authHeader);
            BookingResponse response = bookingService.createBooking(request, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Booking failed: " + e.getMessage()));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyBookings(
            Authentication authentication,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String username = getUsername(authentication, authHeader);
        Long userId = 3L; // rahul_sharma default
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                Long extracted = jwtUtil.extractUserId(authHeader.substring(7));
                if (extracted != null) userId = extracted;
            } catch (Exception ignored) {}
        }
        List<Booking> bookings = bookingService.getBookingsByUser(userId);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping("/cancel/{pnr}")
    public ResponseEntity<?> cancelBooking(
            @PathVariable String pnr,
            @RequestBody(required = false) Map<String, String> body) {
        String reason = (body != null) ? body.get("reason") : "Passenger Cancellation";
        try {
            Map<String, Object> result = cancellationService.cancelBooking(pnr, reason);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
