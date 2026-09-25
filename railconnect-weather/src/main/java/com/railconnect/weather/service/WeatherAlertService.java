package com.railconnect.weather.service;

import com.railconnect.weather.model.WeatherSeverity;
import org.springframework.stereotype.Service;

@Service
public class WeatherAlertService {

    public WeatherSeverity calculateSeverity(Double tempC, Integer rainProb, Double windKmh, Double visibilityKm) {
        if ((rainProb != null && rainProb >= 85) || (windKmh != null && windKmh >= 60.0) || (visibilityKm != null && visibilityKm <= 1.0)) {
            return WeatherSeverity.SEVERE;
        }
        if ((rainProb != null && rainProb >= 70) || (windKmh != null && windKmh >= 35.0) || (tempC != null && tempC >= 43.0)) {
            return WeatherSeverity.WARNING;
        }
        if ((rainProb != null && rainProb >= 40) || (windKmh != null && windKmh >= 20.0)) {
            return WeatherSeverity.CAUTION;
        }
        return WeatherSeverity.NORMAL;
    }

    public String generateAdvisory(WeatherSeverity depSeverity, WeatherSeverity destSeverity, String destName) {
        if (destSeverity == WeatherSeverity.SEVERE || depSeverity == WeatherSeverity.SEVERE) {
            return "CRITICAL ADVISORY: Hazardous weather condition detected along the route. Possible train speed regulations and signal delays. Passengers are advised to monitor live train status.";
        }
        if (destSeverity == WeatherSeverity.WARNING) {
            return "WEATHER WARNING: Significant precipitation or heavy showers expected at " + destName + ". Please carry rain protection and plan local transit in advance.";
        }
        if (destSeverity == WeatherSeverity.CAUTION || depSeverity == WeatherSeverity.CAUTION) {
            return "TRAVEL CAUTION: Scattered showers or moderate winds predicted. Trains are operating on normal schedule.";
        }
        return "OPTIMAL CONDITIONS: Favourable atmospheric conditions along the entire route. Smooth journey anticipated.";
    }
}
