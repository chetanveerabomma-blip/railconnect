package com.railconnect.booking.service;

import com.railconnect.booking.model.Booking;
import com.railconnect.booking.model.BookingPassenger;
import com.railconnect.booking.model.BookingStatus;
import com.railconnect.booking.repository.BookingPassengerRepository;
import com.railconnect.booking.repository.BookingRepository;
import com.railconnect.seatengine.service.SeatLockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CancellationService {

    private final BookingRepository bookingRepository;
    private final BookingPassengerRepository passengerRepository;
    private final SeatLockService seatLockService;

    public CancellationService(BookingRepository bookingRepository,
                               BookingPassengerRepository passengerRepository,
                               SeatLockService seatLockService) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.seatLockService = seatLockService;
    }

    @Transactional
    public Map<String, Object> cancelBooking(String pnrNumber, String reason) {
        Booking booking = bookingRepository.findByPnrNumber(pnrNumber)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with PNR: " + pnrNumber));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Ticket is already cancelled.");
        }

        // Standard Indian Railways Cancellation Deduction:
        // Flat ₹120 per passenger or 20% of total fare, whichever is lower
        int passengerCount = Math.max(1, booking.getPassengers().size());
        double flatCharge = 120.0 * passengerCount;
        double percentageCharge = booking.getTotalFare() * 0.20;
        double cancellationFee = Math.min(flatCharge, percentageCharge);
        double refundAmount = Math.max(0.0, Math.round((booking.getTotalFare() - cancellationFee) * 100.0) / 100.0);

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(reason != null && !reason.isBlank() ? reason : "Passenger requested cancellation");
        booking.setCancellationDate(LocalDateTime.now());
        booking.setRefundAmount(refundAmount);

        // Release seats
        for (BookingPassenger bp : booking.getPassengers()) {
            bp.setStatus(BookingStatus.CANCELLED);
            if (bp.getSeatId() != null) {
                seatLockService.unlockSeat(bp.getSeatId(), null);
            }
            passengerRepository.save(bp);
        }

        bookingRepository.save(booking);

        return Map.of(
                "pnrNumber", pnrNumber,
                "status", "CANCELLED",
                "cancellationFee", cancellationFee,
                "refundAmount", refundAmount,
                "message", "Ticket cancelled successfully. Refund of INR " + refundAmount + " initiated."
        );
    }
}
