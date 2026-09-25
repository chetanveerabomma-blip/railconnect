package com.railconnect.booking.controller;

import com.railconnect.booking.dto.TrainSearchResponse;
import com.railconnect.booking.model.Station;
import com.railconnect.booking.repository.StationRepository;
import com.railconnect.booking.service.TrainSearchService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trains")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TrainSearchController {

    private final TrainSearchService trainSearchService;
    private final StationRepository stationRepository;

    public TrainSearchController(TrainSearchService trainSearchService,
                                 StationRepository stationRepository) {
        this.trainSearchService = trainSearchService;
        this.stationRepository = stationRepository;
    }

    @GetMapping("/stations")
    public ResponseEntity<List<Station>> getAllStations() {
        return ResponseEntity.ok(stationRepository.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrainSearchResponse>> searchTrains(
            @RequestParam("from") String fromStation,
            @RequestParam("to") String toStation,
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate journeyDate) {
        LocalDate date = journeyDate != null ? journeyDate : LocalDate.now();
        List<TrainSearchResponse> responses = trainSearchService.searchTrains(fromStation, toStation, date);
        return ResponseEntity.ok(responses);
    }
}
