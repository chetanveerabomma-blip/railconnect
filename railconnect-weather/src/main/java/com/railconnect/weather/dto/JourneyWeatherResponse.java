package com.railconnect.weather.dto;

public class JourneyWeatherResponse {
    private String departureStation;
    private String destinationStation;
    private String journeyDate;
    private StationWeatherDto departureWeather;
    private StationWeatherDto destinationWeather;
    private String routeAdvisory;
    private String banner;

    public JourneyWeatherResponse() {}

    public JourneyWeatherResponse(String departureStation, String destinationStation, String journeyDate,
                                  StationWeatherDto departureWeather, StationWeatherDto destinationWeather,
                                  String routeAdvisory, String banner) {
        this.departureStation = departureStation;
        this.destinationStation = destinationStation;
        this.journeyDate = journeyDate;
        this.departureWeather = departureWeather;
        this.destinationWeather = destinationWeather;
        this.routeAdvisory = routeAdvisory;
        this.banner = banner;
    }

    public String getDepartureStation() { return departureStation; }
    public void setDepartureStation(String departureStation) { this.departureStation = departureStation; }

    public String getDestinationStation() { return destinationStation; }
    public void setDestinationStation(String destinationStation) { this.destinationStation = destinationStation; }

    public String getJourneyDate() { return journeyDate; }
    public void setJourneyDate(String journeyDate) { this.journeyDate = journeyDate; }

    public StationWeatherDto getDepartureWeather() { return departureWeather; }
    public void setDepartureWeather(StationWeatherDto departureWeather) { this.departureWeather = departureWeather; }

    public StationWeatherDto getDestinationWeather() { return destinationWeather; }
    public void setDestinationWeather(StationWeatherDto destinationWeather) { this.destinationWeather = destinationWeather; }

    public String getRouteAdvisory() { return routeAdvisory; }
    public void setRouteAdvisory(String routeAdvisory) { this.routeAdvisory = routeAdvisory; }

    public String getBanner() { return banner; }
    public void setBanner(String banner) { this.banner = banner; }
}
