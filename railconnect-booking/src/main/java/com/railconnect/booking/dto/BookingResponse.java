package com.railconnect.booking.dto;

import com.railconnect.booking.model.BookingStatus;
import java.util.ArrayList;
import java.util.List;

public class BookingResponse {
    private Long bookingId;
    private String pnrNumber;
    private String ticketId;
    private String trainNumber;
    private String trainName;
    private String fromStationName;
    private String toStationName;
    private String journeyDate;
    private String coachType;
    private Double totalFare;
    private BookingStatus status;
    private String paymentStatus;
    private String transactionId;
    private String qrCodeText;
    private String qrCodeImageBase64;
    private List<PassengerTicketInfoDto> passengers = new ArrayList<>();
    private String message;

    public BookingResponse() {}

    public static class PassengerTicketInfoDto {
        private Long id;
        private String name;
        private int age;
        private String gender;
        private String coach;
        private Integer seatNumber;
        private String berthType;
        private BookingStatus status;

        public PassengerTicketInfoDto() {}

        public PassengerTicketInfoDto(Long id, String name, int age, String gender, String coach, Integer seatNumber, String berthType, BookingStatus status) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.coach = coach;
            this.seatNumber = seatNumber;
            this.berthType = berthType;
            this.status = status;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }

        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }

        public String getCoach() { return coach; }
        public void setCoach(String coach) { this.coach = coach; }

        public Integer getSeatNumber() { return seatNumber; }
        public void setSeatNumber(Integer seatNumber) { this.seatNumber = seatNumber; }

        public String getBerthType() { return berthType; }
        public void setBerthType(String berthType) { this.berthType = berthType; }

        public BookingStatus getStatus() { return status; }
        public void setStatus(BookingStatus status) { this.status = status; }
    }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public String getPnrNumber() { return pnrNumber; }
    public void setPnrNumber(String pnrNumber) { this.pnrNumber = pnrNumber; }

    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }

    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }

    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }

    public String getFromStationName() { return fromStationName; }
    public void setFromStationName(String fromStationName) { this.fromStationName = fromStationName; }

    public String getToStationName() { return toStationName; }
    public void setToStationName(String toStationName) { this.toStationName = toStationName; }

    public String getJourneyDate() { return journeyDate; }
    public void setJourneyDate(String journeyDate) { this.journeyDate = journeyDate; }

    public String getCoachType() { return coachType; }
    public void setCoachType(String coachType) { this.coachType = coachType; }

    public Double getTotalFare() { return totalFare; }
    public void setTotalFare(Double totalFare) { this.totalFare = totalFare; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getQrCodeText() { return qrCodeText; }
    public void setQrCodeText(String qrCodeText) { this.qrCodeText = qrCodeText; }

    public String getQrCodeImageBase64() { return qrCodeImageBase64; }
    public void setQrCodeImageBase64(String qrCodeImageBase64) { this.qrCodeImageBase64 = qrCodeImageBase64; }

    public List<PassengerTicketInfoDto> getPassengers() { return passengers; }
    public void setPassengers(List<PassengerTicketInfoDto> passengers) { this.passengers = passengers; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
