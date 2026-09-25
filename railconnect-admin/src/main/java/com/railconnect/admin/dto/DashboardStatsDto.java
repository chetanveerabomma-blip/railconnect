package com.railconnect.admin.dto;

import java.util.HashMap;
import java.util.Map;

public class DashboardStatsDto {
    private long totalTrains;
    private long totalStations;
    private long todaysBookings;
    private long activePassengers;
    private long availableSeats;
    private long cancelledTickets;
    private long pendingExchangeRequests;
    private double totalRevenue;

    // Chart aggregations
    private Map<String, Long> dailyBookings = new HashMap<>();
    private Map<String, Double> revenueTrend = new HashMap<>();
    private Map<String, Double> trainOccupancy = new HashMap<>();
    private Map<String, Long> classUsage = new HashMap<>();
    private double cancellationRate;

    public DashboardStatsDto() {}

    public long getTotalTrains() { return totalTrains; }
    public void setTotalTrains(long totalTrains) { this.totalTrains = totalTrains; }

    public long getTotalStations() { return totalStations; }
    public void setTotalStations(long totalStations) { this.totalStations = totalStations; }

    public long getTodaysBookings() { return todaysBookings; }
    public void setTodaysBookings(long todaysBookings) { this.todaysBookings = todaysBookings; }

    public long getActivePassengers() { return activePassengers; }
    public void setActivePassengers(long activePassengers) { this.activePassengers = activePassengers; }

    public long getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(long availableSeats) { this.availableSeats = availableSeats; }

    public long getCancelledTickets() { return cancelledTickets; }
    public void setCancelledTickets(long cancelledTickets) { this.cancelledTickets = cancelledTickets; }

    public long getPendingExchangeRequests() { return pendingExchangeRequests; }
    public void setPendingExchangeRequests(long pendingExchangeRequests) { this.pendingExchangeRequests = pendingExchangeRequests; }

    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }

    public Map<String, Long> getDailyBookings() { return dailyBookings; }
    public void setDailyBookings(Map<String, Long> dailyBookings) { this.dailyBookings = dailyBookings; }

    public Map<String, Double> getRevenueTrend() { return revenueTrend; }
    public void setRevenueTrend(Map<String, Double> revenueTrend) { this.revenueTrend = revenueTrend; }

    public Map<String, Double> getTrainOccupancy() { return trainOccupancy; }
    public void setTrainOccupancy(Map<String, Double> trainOccupancy) { this.trainOccupancy = trainOccupancy; }

    public Map<String, Long> getClassUsage() { return classUsage; }
    public void setClassUsage(Map<String, Long> classUsage) { this.classUsage = classUsage; }

    public double getCancellationRate() { return cancellationRate; }
    public void setCancellationRate(double cancellationRate) { this.cancellationRate = cancellationRate; }
}
