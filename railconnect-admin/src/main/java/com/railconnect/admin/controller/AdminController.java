package com.railconnect.admin.controller;

import com.railconnect.admin.dto.*;
import com.railconnect.admin.service.AdminService;
import com.railconnect.auth.jwt.JwtUtil;
import com.railconnect.booking.model.FareRule;
import com.railconnect.booking.model.Station;
import com.railconnect.booking.model.Train;
import com.railconnect.booking.model.TrainRoute;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {

    private final AdminService adminService;
    private final JwtUtil jwtUtil;

    public AdminController(AdminService adminService, JwtUtil jwtUtil) {
        this.adminService = adminService;
        this.jwtUtil = jwtUtil;
    }

    private Long getAdminId(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                Long id = jwtUtil.extractUserId(authHeader.substring(7));
                if (id != null) return id;
            } catch (Exception ignored) {}
        }
        return 1L; // default admin id
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/trains")
    public ResponseEntity<List<Train>> getAllTrains() {
        return ResponseEntity.ok(adminService.getAllTrains());
    }

    @PostMapping("/trains")
    public ResponseEntity<?> addTrain(
            @Valid @RequestBody TrainManagementRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            Long adminId = getAdminId(authHeader);
            Train train = adminService.addTrain(request, adminId);
            return ResponseEntity.status(HttpStatus.CREATED).body(train);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/trains/{id}")
    public ResponseEntity<?> updateTrain(
            @PathVariable("id") Long id,
            @RequestBody TrainManagementRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            Long adminId = getAdminId(authHeader);
            Train train = adminService.updateTrain(id, request, adminId);
            return ResponseEntity.ok(train);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/trains/{id}")
    public ResponseEntity<?> removeTrain(
            @PathVariable("id") Long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            Long adminId = getAdminId(authHeader);
            adminService.removeTrain(id, adminId);
            return ResponseEntity.ok(Map.of("message", "Train deactivated / removed successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/trains/{trainId}/routes")
    public ResponseEntity<List<RouteStopDto>> getTrainRoutes(@PathVariable("trainId") Long trainId) {
        return ResponseEntity.ok(adminService.getTrainRoutes(trainId));
    }

    @PostMapping("/trains/{trainId}/routes")
    public ResponseEntity<?> addRouteStop(
            @PathVariable("trainId") Long trainId,
            @RequestBody RouteStopRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            Long adminId = getAdminId(authHeader);
            TrainRoute route = adminService.addRouteStop(trainId, request, adminId);
            return ResponseEntity.status(HttpStatus.CREATED).body(route);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/trains/routes/{routeId}")
    public ResponseEntity<?> removeRouteStop(
            @PathVariable("routeId") Long routeId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            Long adminId = getAdminId(authHeader);
            adminService.removeRouteStop(routeId, adminId);
            return ResponseEntity.ok(Map.of("message", "Route stop removed successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/stations")
    public ResponseEntity<?> addStation(
            @Valid @RequestBody StationManagementRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            Long adminId = getAdminId(authHeader);
            Station station = adminService.addStation(request, adminId);
            return ResponseEntity.status(HttpStatus.CREATED).body(station);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/fares")
    public ResponseEntity<List<FareRule>> getAllFares() {
        return ResponseEntity.ok(adminService.getAllFares());
    }

    @PostMapping("/fares")
    public ResponseEntity<?> configureFare(
            @Valid @RequestBody FareConfigRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            Long adminId = getAdminId(authHeader);
            FareRule rule = adminService.configureFare(request, adminId);
            return ResponseEntity.ok(rule);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/audits")
    public ResponseEntity<?> getAuditLogs() {
        return ResponseEntity.ok(adminService.getAuditLogs());
    }
}
