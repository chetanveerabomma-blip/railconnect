package com.railconnect.weather.service;

import com.railconnect.weather.dto.JourneyWeatherResponse;
import com.railconnect.weather.dto.StationWeatherDto;
import com.railconnect.weather.model.WeatherSeverity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    @Value("${railconnect.weather.api-key:}")
    private String apiKey;

    @Value("${railconnect.weather.api-url:https://api.openweathermap.org/data/2.5/weather}")
    private String apiUrl;

    private final WeatherAlertService alertService;

    // Realistic Demo Station Database for Indian Railways
    private final Map<String, StationWeatherDto> mockStationWeatherMap = new HashMap<>();

    public WeatherService(WeatherAlertService alertService) {
        this.alertService = alertService;
        initMockData();
    }

    private void initMockData() {
        // TRICHY (TPJ): 32°C, Partly Cloudy, Rain: 20%, NORMAL
        mockStationWeatherMap.put("TPJ", new StationWeatherDto(
                "TPJ", "Tiruchchirappalli Junction (Trichy)", 32.0, "Partly Cloudy",
                20, 62, 14.5, 9.5, WeatherSeverity.NORMAL,
                "Clear skies with pleasant travelling conditions in Tiruchirappalli.",
                true, "Demo Weather Data"
        ));

        // CHENNAI (MAS): 29°C, Heavy Rain, Rain: 80%, WARNING
        mockStationWeatherMap.put("MAS", new StationWeatherDto(
                "MAS", "Chennai Central", 29.0, "Heavy Rain",
                80, 92, 28.0, 4.0, WeatherSeverity.WARNING,
                "High precipitation forecast. Coastal gusts expected near Chennai Central.",
                true, "Demo Weather Data"
        ));

        // NEW DELHI (NDLS): 34.5°C, Sunny, Rain: 10%, NORMAL
        mockStationWeatherMap.put("NDLS", new StationWeatherDto(
                "NDLS", "New Delhi Railway Station", 34.5, "Sunny",
                10, 45, 12.0, 10.0, WeatherSeverity.NORMAL,
                "Normal dry weather in New Delhi capital area.",
                true, "Demo Weather Data"
        ));

        // MUMBAI CENTRAL (BCT): 28.0°C, Moderate Rain, Rain: 65%, CAUTION
        mockStationWeatherMap.put("BCT", new StationWeatherDto(
                "BCT", "Mumbai Central", 28.0, "Moderate Rain",
                65, 88, 22.0, 6.0, WeatherSeverity.CAUTION,
                "Scattered showers expected in Mumbai Central vicinity.",
                true, "Demo Weather Data"
        ));

        // BENGALURU (SBC): 25.5°C, Pleasant Breeze, Rain: 15%, NORMAL
        mockStationWeatherMap.put("SBC", new StationWeatherDto(
                "SBC", "KSR Bengaluru City", 25.5, "Pleasant Breeze",
                15, 68, 10.0, 10.0, WeatherSeverity.NORMAL,
                "Crisp and cool journey weather across Bengaluru corridor.",
                true, "Demo Weather Data"
        ));

        // HOWRAH (HWH): 31.0°C, Thunderstorm Forecast, Rain: 75%, WARNING
        mockStationWeatherMap.put("HWH", new StationWeatherDto(
                "HWH", "Howrah Junction (Kolkata)", 31.0, "Thunderstorm Forecast",
                75, 89, 36.0, 5.0, WeatherSeverity.WARNING,
                "Convective storm cells detected across Gangetic delta area.",
                true, "Demo Weather Data"
        ));
    }

    public StationWeatherDto getStationWeather(String stationCode) {
        String code = stationCode != null ? stationCode.trim().toUpperCase() : "NDLS";

        // Check if real API key configured; if not, use mock data
        if (apiKey != null && !apiKey.isBlank() && !apiKey.equals("YOUR_API_KEY_HERE")) {
            try {
                // Production external API invocation (Safe HTTP client)
                // If call fails, silently fallback to mock data
            } catch (Exception ignored) {
                // Fallback to mock
            }
        }

        // Return matched mock station or generate consistent default
        if (mockStationWeatherMap.containsKey(code)) {
            return mockStationWeatherMap.get(code);
        }

        // Default mock profile for any other station
        WeatherSeverity severity = WeatherSeverity.NORMAL;
        return new StationWeatherDto(
                code, code + " Railway Station", 30.0, "Clear Skies",
                15, 55, 12.0, 10.0, severity,
                "Standard meteorological conditions at " + code,
                true, "Demo Weather Data"
        );
    }

    public JourneyWeatherResponse getJourneyWeather(String fromStation, String toStation, String journeyDate) {
        StationWeatherDto departureWeather = getStationWeather(fromStation);
        StationWeatherDto destinationWeather = getStationWeather(toStation);

        String advisory = alertService.generateAdvisory(
                departureWeather.getSeverity(),
                destinationWeather.getSeverity(),
                destinationWeather.getStationName()
        );

        String banner = (departureWeather.isMockData() || destinationWeather.isMockData())
                ? "Demo Weather Data"
                : "Live Meteorological Feed";

        return new JourneyWeatherResponse(
                fromStation,
                toStation,
                journeyDate != null ? journeyDate : "Today",
                departureWeather,
                destinationWeather,
                advisory,
                banner
        );
    }
}
