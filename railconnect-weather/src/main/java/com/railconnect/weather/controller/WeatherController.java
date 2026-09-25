package com.railconnect.weather.controller;

import com.railconnect.weather.dto.JourneyWeatherResponse;
import com.railconnect.weather.dto.StationWeatherDto;
import com.railconnect.weather.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*", maxAge = 3600)
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/journey")
    public ResponseEntity<JourneyWeatherResponse> getJourneyWeather(
            @RequestParam(name = "from", defaultValue = "TPJ") String fromStation,
            @RequestParam(name = "to", defaultValue = "MAS") String toStation,
            @RequestParam(name = "date", required = false) String journeyDate) {
        JourneyWeatherResponse response = weatherService.getJourneyWeather(fromStation, toStation, journeyDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/station/{code}")
    public ResponseEntity<StationWeatherDto> getStationWeather(@PathVariable String code) {
        StationWeatherDto response = weatherService.getStationWeather(code);
        return ResponseEntity.ok(response);
    }
}
