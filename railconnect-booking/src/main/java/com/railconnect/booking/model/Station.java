package com.railconnect.booking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "stations")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 20)
    private String zone = "SR";

    @Column(name = "platform_count")
    private Integer platformCount = 4;

    private Double latitude;
    private Double longitude;

    public Station() {}

    public Station(String code, String name, String city, String state, String zone, Integer platformCount) {
        this.code = code;
        this.name = name;
        this.city = city;
        this.state = state;
        this.zone = zone;
        this.platformCount = platformCount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
