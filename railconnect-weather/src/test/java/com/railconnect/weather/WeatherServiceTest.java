package com.railconnect.weather;

import com.railconnect.weather.dto.JourneyWeatherResponse;
import com.railconnect.weather.dto.StationWeatherDto;
import com.railconnect.weather.model.WeatherSeverity;
import com.railconnect.weather.service.WeatherAlertService;
import com.railconnect.weather.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherServiceTest {

    private WeatherAlertService alertService;
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        alertService = new WeatherAlertService();
        weatherService = new WeatherService(alertService);
    }

    @Test
    void testTrichyNormalAndChennaiWarning() {
        StationWeatherDto trichy = weatherService.getStationWeather("TPJ");
        assertNotNull(trichy);
        assertEquals(32.0, trichy.getTemperatureC());
        assertEquals(20, trichy.getRainProbability());
        assertEquals(WeatherSeverity.NORMAL, trichy.getSeverity());
        assertTrue(trichy.isMockData());
        assertEquals("Demo Weather Data", trichy.getDataSource());

        StationWeatherDto chennai = weatherService.getStationWeather("MAS");
        assertNotNull(chennai);
        assertEquals(29.0, chennai.getTemperatureC());
        assertEquals(80, chennai.getRainProbability());
        assertEquals(WeatherSeverity.WARNING, chennai.getSeverity());
        assertTrue(chennai.isMockData());
    }

    @Test
    void testJourneyWeatherResponseBanner() {
        JourneyWeatherResponse journey = weatherService.getJourneyWeather("TPJ", "MAS", "2026-10-10");
        assertNotNull(journey);
        assertEquals("Demo Weather Data", journey.getBanner());
        assertNotNull(journey.getRouteAdvisory());
        assertTrue(journey.getRouteAdvisory().contains("WARNING"));
    }

    @Test
    void testSeverityCalculation() {
        assertEquals(WeatherSeverity.NORMAL, alertService.calculateSeverity(28.0, 10, 10.0, 10.0));
        assertEquals(WeatherSeverity.CAUTION, alertService.calculateSeverity(30.0, 45, 12.0, 8.0));
        assertEquals(WeatherSeverity.WARNING, alertService.calculateSeverity(32.0, 75, 25.0, 5.0));
        assertEquals(WeatherSeverity.SEVERE, alertService.calculateSeverity(35.0, 90, 65.0, 0.8));
    }
}
