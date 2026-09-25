package com.railconnect.admin.controller;

import com.railconnect.admin.dto.VerifyTicketRequest;
import com.railconnect.admin.model.TicketVerification;
import com.railconnect.admin.service.AdminService;
import com.railconnect.auth.jwt.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TicketVerificationController {

    private final AdminService adminService;
    private final JwtUtil jwtUtil;

    public TicketVerificationController(AdminService adminService, JwtUtil jwtUtil) {
        this.adminService = adminService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyTicket(
            @Valid @RequestBody VerifyTicketRequest request,
            Authentication authentication,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String inspectorUsername = "inspector_anand";
            if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
                inspectorUsername = authentication.getName();
            } else if (authHeader != null && authHeader.startsWith("Bearer ")) {
                try {
                    inspectorUsername = jwtUtil.extractUsername(authHeader.substring(7));
                } catch (Exception ignored) {}
            }

            TicketVerification verification = adminService.verifyTicket(request, inspectorUsername);
            return ResponseEntity.ok(Map.of(
                    "status", "VERIFIED",
                    "pnr", verification.getBooking().getPnrNumber(),
                    "trainNumber", verification.getBooking().getTrain().getTrainNumber(),
                    "journeyDate", verification.getBooking().getJourneyDate(),
                    "verificationStatus", verification.getVerificationStatus(),
                    "verifiedAt", verification.getVerificationTimestamp(),
                    "inspector", verification.getInspector().getUsername(),
                    "message", "Digital Travel Pass authenticated successfully."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage(), "status", "INVALID"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage(), "status", "ERROR"));
        }
    }
}
