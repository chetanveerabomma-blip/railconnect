package com.railconnect.weather.dto;

import com.railconnect.weather.model.WeatherSeverity;

public class StationWeatherDto {
    private String stationCode;
    private String stationName;
    private Double temperatureC;
    private String condition;
    private Integer rainProbability;
    private Integer humidityPercent;
    private Double windSpeedKmh;
    private Double visibilityKm;
    private WeatherSeverity severity;
    private String alertMessage;
    private boolean isMockData;
    private String dataSource;

    public StationWeatherDto() {}

    public StationWeatherDto(String stationCode, String stationName, Double temperatureC, String condition,
                             Integer rainProbability, Integer humidityPercent, Double windSpeedKmh,
                             Double visibilityKm, WeatherSeverity severity, String alertMessage,
                             boolean isMockData, String dataSource) {
        this.stationCode = stationCode;
        this.stationName = stationName;
        this.temperatureC = temperatureC;
        this.condition = condition;
        this.rainProbability = rainProbability;
        this.humidityPercent = humidityPercent;
        this.windSpeedKmh = windSpeedKmh;
        this.visibilityKm = visibilityKm;
        this.severity = severity;
        this.alertMessage = alertMessage;
        this.isMockData = isMockData;
        this.dataSource = dataSource;
    }

    public String getStationCode() { return stationCode; }
    public void setStationCode(String stationCode) { this.stationCode = stationCode; }

    public String getStationName() { return stationName; }
    public void setStationName(String stationName) { this.stationName = stationName; }

    public Double getTemperatureC() { return temperatureC; }
    public void setTemperatureC(Double temperatureC) { this.temperatureC = temperatureC; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

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

    public boolean isMockData() { return isMockData; }
    public void setMockData(boolean mockData) { isMockData = mockData; }

    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
}
