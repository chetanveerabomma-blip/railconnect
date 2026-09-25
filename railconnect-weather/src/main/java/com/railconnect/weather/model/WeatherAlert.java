package com.railconnect.weather.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "weather_alerts")
public class WeatherAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "station_id", nullable = false)
    private Long stationId;

    @Column(name = "temperature_c", nullable = false)
    private Double temperatureC;

    @Column(name = "weather_condition", nullable = false, length = 100)
    private String weatherCondition;

    @Column(name = "rain_probability", nullable = false)
    private Integer rainProbability = 0;

    @Column(name = "humidity_percent", nullable = false)
    private Integer humidityPercent = 50;

    @Column(name = "wind_speed_kmh", nullable = false)
    private Double windSpeedKmh = 10.0;

    @Column(name = "visibility_km", nullable = false)
    private Double visibilityKm = 10.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WeatherSeverity severity = WeatherSeverity.NORMAL;

    @Column(name = "alert_message", nullable = false)
    private String alertMessage;

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt = LocalDateTime.now();

    public WeatherAlert() {}

    public WeatherAlert(Long stationId, Double temperatureC, String weatherCondition, Integer rainProbability, Integer humidityPercent, Double windSpeedKmh, Double visibilityKm, WeatherSeverity severity, String alertMessage) {
        this.stationId = stationId;
        this.temperatureC = temperatureC;
        this.weatherCondition = weatherCondition;
        this.rainProbability = rainProbability;
        this.humidityPercent = humidityPercent;
        this.windSpeedKmh = windSpeedKmh;
        this.visibilityKm = visibilityKm;
        this.severity = severity;
        this.alertMessage = alertMessage;
        this.recordedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStationId() { return stationId; }
    public void setStationId(Long stationId) { this.stationId = stationId; }

    public Double getTemperatureC() { return temperatureC; }
    public void setTemperatureC(Double temperatureC) { this.temperatureC = temperatureC; }

    public String getWeatherCondition() { return weatherCondition; }
    public void setWeatherCondition(String weatherCondition) { this.weatherCondition = weatherCondition; }

    public Integer getRainProbability() { return rainProbability; }
    public void setRainProbability(Integer rainProbability) { this.rainProbability = rainProbability; }

    public Integer getHumidityPercent() { return humidityPercent; }
    public void setHumidityPercent(Integer humidityPercent) { this.humidityPercent = humidityPercent; }

    public Double getWindSpeedKmh() { return windSpeedKmh; }
    public void setWindSpeedKmh(Double windSpeedKmh) { this.windSpeedKmh = windSpeedKmh; }

    public Double getVisibilityKm() { return visibilityKm; }
    public void setVisibilityKm(Double visibilityKm) { this.visibilityKm = visibilityKm; }

    public WeatherSeverity getSeverity() { return severity; }
    public void setSeverity(WeatherSeverity severity) { this.severity = severity; }

    public String getAlertMessage() { return alertMessage; }
    public void setAlertMessage(String alertMessage) { this.alertMessage = alertMessage; }

    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}
