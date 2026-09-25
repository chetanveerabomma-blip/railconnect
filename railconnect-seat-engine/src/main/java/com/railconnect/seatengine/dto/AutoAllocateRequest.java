package com.railconnect.seatengine.dto;

import java.util.ArrayList;
import java.util.List;

public class AutoAllocateRequest {
    private Long trainId;
    private String coachType;
    private String journeyDate;
    private List<PassengerPreference> passengers = new ArrayList<>();

    public AutoAllocateRequest() {}

    public static class PassengerPreference {
        private String name;
        private int age;
        private String gender;
        private String preference; // LOWER, MIDDLE, UPPER, SIDE_LOWER, SIDE_UPPER, WINDOW, AISLE, NO_PREFERENCE

        public PassengerPreference() {}

        public PassengerPreference(String name, int age, String gender, String preference) {
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.preference = preference;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }

        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }

        public String getPreference() { return preference; }
        public void setPreference(String preference) { this.preference = preference; }

        public boolean isSeniorCitizen() {
            return age >= 60;
        }
    }

    public Long getTrainId() { return trainId; }
    public void setTrainId(Long trainId) { this.trainId = trainId; }

    public String getCoachType() { return coachType; }
    public void setCoachType(String coachType) { this.coachType = coachType; }

    public String getJourneyDate() { return journeyDate; }
    public void setJourneyDate(String journeyDate) { this.journeyDate = journeyDate; }

    public List<PassengerPreference> getPassengers() { return passengers; }
    public void setPassengers(List<PassengerPreference> passengers) { this.passengers = passengers; }
}
