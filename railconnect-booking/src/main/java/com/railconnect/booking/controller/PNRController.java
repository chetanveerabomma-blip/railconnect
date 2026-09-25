package com.railconnect.booking.controller;

import com.railconnect.booking.dto.BookingResponse;
import com.railconnect.booking.model.Booking;
import com.railconnect.booking.model.BookingPassenger;
import com.railconnect.booking.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pnr")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PNRController {

    private final BookingService bookingService;

    public PNRController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{pnr}")
    public ResponseEntity<?> getPnrStatus(@PathVariable String pnr) {
        Optional<Booking> bookingOpt = bookingService.getBookingByPnr(pnr);
        if (bookingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No booking records found for 10-digit PNR: " + pnr));
        }

        Booking booking = bookingOpt.get();
        String ticketId = "TKT-" + booking.getPnrNumber().substring(0, 4) + "-" + booking.getId();
        String qrPayload = String.format("RAILCONNECT|PNR:%s|TICKET:%s|TRAIN:%s|FROM:%s|TO:%s|DATE:%s|STATUS:%s",
                booking.getPnrNumber(), ticketId, booking.getTrain().getTrainNumber(),
                booking.getFromStation().getCode(), booking.getToStation().getCode(),
                booking.getJourneyDate(), booking.getStatus());

        List<BookingResponse.PassengerTicketInfoDto> passengers = new ArrayList<>();
        for (BookingPassenger bp : booking.getPassengers()) {
            passengers.add(new BookingResponse.PassengerTicketInfoDto(
                    bp.getId(),
                    bp.getPassengerName(),
                    bp.getPassengerAge(),
                    bp.getPassengerGender(),
                    bp.getAllocatedCoach(),
                    bp.getAllocatedSeatNumber(),
                    bp.getAllocatedBerthType(),
                    bp.getStatus()
            ));
        }

        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getId());
        response.setPnrNumber(booking.getPnrNumber());
        response.setTicketId(ticketId);
        response.setTrainNumber(booking.getTrain().getTrainNumber());
        response.setTrainName(booking.getTrain().getTrainName());
        response.setFromStationName(booking.getFromStation().getName() + " (" + booking.getFromStation().getCode() + ")");
        response.setToStationName(booking.getToStation().getName() + " (" + booking.getToStation().getCode() + ")");
        response.setJourneyDate(booking.getJourneyDate().toString());
        response.setCoachType(booking.getCoachType());
        response.setTotalFare(booking.getTotalFare());
        response.setStatus(booking.getStatus());
        response.setQrCodeText(qrPayload);
        response.setPassengers(passengers);
        response.setMessage("PNR enquiry retrieved successfully.");

        return ResponseEntity.ok(response);
    }
}
