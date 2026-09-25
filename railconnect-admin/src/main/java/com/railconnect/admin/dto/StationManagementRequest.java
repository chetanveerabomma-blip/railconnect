package com.railconnect.admin.dto;

import jakarta.validation.constraints.NotBlank;

public class StationManagementRequest {
    @NotBlank(message = "Station code is required")
    private String code;

    @NotBlank(message = "Station name is required")
    private String name;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    private String zone = "SR";
    private Integer platformCount = 4;
    private Double latitude;
    private Double longitude;

    public StationManagementRequest() {}

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }

    public Integer getPlatformCount() { return platformCount; }
    public void setPlatformCount(Integer platformCount) { this.platformCount = platformCount; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}
