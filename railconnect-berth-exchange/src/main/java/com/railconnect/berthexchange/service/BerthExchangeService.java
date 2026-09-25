package com.railconnect.berthexchange.service;

import com.railconnect.berthexchange.dto.ExchangeInitiateRequest;
import com.railconnect.berthexchange.dto.ExchangeRequestDto;
import com.railconnect.berthexchange.model.AuditLog;
import com.railconnect.berthexchange.model.BerthExchangeRequest;
import com.railconnect.berthexchange.model.ExchangeStatus;
import com.railconnect.berthexchange.repository.AuditLogRepository;
import com.railconnect.berthexchange.repository.ExchangeRepository;
import com.railconnect.booking.model.Booking;
import com.railconnect.booking.model.BookingPassenger;
import com.railconnect.booking.model.BookingStatus;
import com.railconnect.booking.repository.BookingPassengerRepository;
import com.railconnect.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BerthExchangeService {

    private final ExchangeRepository exchangeRepository;
    private final AuditLogRepository auditLogRepository;
    private final BookingRepository bookingRepository;
    private final BookingPassengerRepository passengerRepository;
    private final ExchangeRuleEngine ruleEngine;

    public BerthExchangeService(ExchangeRepository exchangeRepository,
                                AuditLogRepository auditLogRepository,
                                BookingRepository bookingRepository,
                                BookingPassengerRepository passengerRepository,
                                ExchangeRuleEngine ruleEngine) {
        this.exchangeRepository = exchangeRepository;
        this.auditLogRepository = auditLogRepository;
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.ruleEngine = ruleEngine;
    }

    @Transactional
    public ExchangeRequestDto initiateExchange(ExchangeInitiateRequest req, Long userId) {
        Booking reqBooking = bookingRepository.findById(req.getRequesterBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Requester booking not found"));
        BookingPassenger reqPassenger = passengerRepository.findById(req.getRequesterPassengerId())
                .orElseThrow(() -> new IllegalArgumentException("Requester passenger not found"));

        Booking targetBooking = bookingRepository.findById(req.getTargetBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Target booking not found"));
        BookingPassenger targetPassenger = passengerRepository.findById(req.getTargetPassengerId())
                .orElseThrow(() -> new IllegalArgumentException("Target passenger not found"));

        // Validate all 10 rules
        ruleEngine.validateExchangeEligibility(reqBooking, reqPassenger, targetBooking, targetPassenger);

        BerthExchangeRequest exchange = new BerthExchangeRequest();
        exchange.setRequesterBooking(reqBooking);
        exchange.setRequesterPassenger(reqPassenger);
        exchange.setTargetBooking(targetBooking);
        exchange.setTargetPassenger(targetPassenger);
        exchange.setTrain(reqBooking.getTrain());
        exchange.setJourneyDate(reqBooking.getJourneyDate());
        exchange.setStatus(ExchangeStatus.REQUESTED);
        exchange.setRequestReason(req.getReason() != null ? req.getReason() : "Berth swap requested by co-passenger");
        exchange.setCreatedAt(LocalDateTime.now());
        exchange.setUpdatedAt(LocalDateTime.now());

        BerthExchangeRequest saved = exchangeRepository.save(exchange);

        // Rule 10: Log to audit
        auditLogRepository.save(new AuditLog(
                userId, "INITIATE_BERTH_EXCHANGE", "BERTH_EXCHANGE",
                String.valueOf(saved.getId()), null,
                "Request created: PNR " + reqBooking.getPnrNumber() + " -> PNR " + targetBooking.getPnrNumber(),
                "127.0.0.1"
        ));

        return toDto(saved);
    }

    @Transactional
    public ExchangeRequestDto respondToRequest(Long requestId, boolean accept, String reason, Long userId) {
        BerthExchangeRequest exchange = exchangeRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Exchange request not found: " + requestId));

        if (exchange.getStatus() != ExchangeStatus.REQUESTED) {
            throw new IllegalStateException("Cannot respond to request with status: " + exchange.getStatus());
        }

        if (accept) {
            // Target passenger accepted -> Move to PENDING_ADMIN for safety compliance
            exchange.setStatus(ExchangeStatus.PENDING_ADMIN);
            exchange.setUpdatedAt(LocalDateTime.now());
        } else {
            exchange.setStatus(ExchangeStatus.REJECTED);
            exchange.setRequestReason(reason != null ? reason : "Target passenger declined berth swap.");
            exchange.setUpdatedAt(LocalDateTime.now());
        }

        BerthExchangeRequest updated = exchangeRepository.save(exchange);

        auditLogRepository.save(new AuditLog(
                userId, "RESPOND_BERTH_EXCHANGE", "BERTH_EXCHANGE",
                String.valueOf(requestId), "REQUESTED", updated.getStatus().name(), "127.0.0.1"
        ));

        return toDto(updated);
    }

    @Transactional
    public ExchangeRequestDto adminApprove(Long requestId, boolean approve, String adminNotes, Long adminUserId) {
        BerthExchangeRequest exchange = exchangeRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Exchange request not found: " + requestId));

        if (exchange.getStatus() != ExchangeStatus.PENDING_ADMIN && exchange.getStatus() != ExchangeStatus.REQUESTED) {
            throw new IllegalStateException("Only pending requests can be reviewed by administrator.");
        }

        if (approve) {
            // Atomic Berth Swap Execution
            BookingPassenger reqP = exchange.getRequesterPassenger();
            BookingPassenger tarP = exchange.getTargetPassenger();

            Long tempSeatId = reqP.getSeatId();
            String tempCoach = reqP.getAllocatedCoach();
            Integer tempSeatNum = reqP.getAllocatedSeatNumber();
            String tempBerth = reqP.getAllocatedBerthType();

            // Swap Requester -> Target
            reqP.setSeatId(tarP.getSeatId());
            reqP.setAllocatedCoach(tarP.getAllocatedCoach());
            reqP.setAllocatedSeatNumber(tarP.getAllocatedSeatNumber());
            reqP.setAllocatedBerthType(tarP.getAllocatedBerthType());

            // Swap Target -> Requester
            tarP.setSeatId(tempSeatId);
            tarP.setAllocatedCoach(tempCoach);
            tarP.setAllocatedSeatNumber(tempSeatNum);
            tarP.setAllocatedBerthType(tempBerth);

            passengerRepository.save(reqP);
            passengerRepository.save(tarP);

            exchange.setStatus(ExchangeStatus.COMPLETED);
            exchange.setAdminNotes(adminNotes != null ? adminNotes : "Approved by Railway Administrator.");
        } else {
            exchange.setStatus(ExchangeStatus.REJECTED);
            exchange.setAdminNotes(adminNotes != null ? adminNotes : "Rejected by Railway Administrator.");
        }

        exchange.setUpdatedAt(LocalDateTime.now());
        BerthExchangeRequest saved = exchangeRepository.save(exchange);

        auditLogRepository.save(new AuditLog(
                adminUserId, "ADMIN_BERTH_EXCHANGE", "BERTH_EXCHANGE",
                String.valueOf(requestId), "PENDING_ADMIN", saved.getStatus().name(), "127.0.0.1"
        ));

        return toDto(saved);
    }

    public List<BookingPassenger> findEligiblePassengersForExchange(Long trainId, LocalDate journeyDate, String coachType, Long excludeBookingId) {
        List<Booking> bookings = bookingRepository.findByTrainIdAndJourneyDateAndStatusNot(trainId, journeyDate, BookingStatus.CANCELLED);
        List<BookingPassenger> eligible = new ArrayList<>();
        for (Booking b : bookings) {
            if (excludeBookingId != null && b.getId().equals(excludeBookingId)) {
                continue;
            }
            if (b.getCoachType().equalsIgnoreCase(coachType)) {
                for (BookingPassenger bp : b.getPassengers()) {
                    if (bp.getStatus() == BookingStatus.CONFIRMED && bp.getSeatId() != null) {
                        eligible.add(bp);
                    }
                }
            }
        }
        return eligible;
    }

    public List<ExchangeRequestDto> getRequestsByUser(Long userId) {
        return exchangeRepository.findByUserInvolved(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ExchangeRequestDto> getAllRequests() {
        return exchangeRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ExchangeRequestDto toDto(BerthExchangeRequest e) {
        ExchangeRequestDto dto = new ExchangeRequestDto();
        dto.setId(e.getId());
        dto.setRequesterBookingId(e.getRequesterBooking().getId());
        dto.setRequesterPnr(e.getRequesterBooking().getPnrNumber());
        dto.setRequesterPassengerName(e.getRequesterPassenger().getPassengerName());
        dto.setRequesterCoach(e.getRequesterPassenger().getAllocatedCoach());
        dto.setRequesterSeatNumber(e.getRequesterPassenger().getAllocatedSeatNumber());
        dto.setRequesterBerthType(e.getRequesterPassenger().getAllocatedBerthType());

        dto.setTargetBookingId(e.getTargetBooking().getId());
        dto.setTargetPnr(e.getTargetBooking().getPnrNumber());
        dto.setTargetPassengerName(e.getTargetPassenger().getPassengerName());
        dto.setTargetCoach(e.getTargetPassenger().getAllocatedCoach());
        dto.setTargetSeatNumber(e.getTargetPassenger().getAllocatedSeatNumber());
        dto.setTargetBerthType(e.getTargetPassenger().getAllocatedBerthType());

        dto.setTrainNumber(e.getTrain().getTrainNumber());
        dto.setTrainName(e.getTrain().getTrainName());
        dto.setJourneyDate(e.getJourneyDate().toString());
        dto.setStatus(e.getStatus());
        dto.setRequestReason(e.getRequestReason());
        dto.setAdminNotes(e.getAdminNotes());
        dto.setCreatedAt(e.getCreatedAt() != null ? e.getCreatedAt().toString() : "");
        return dto;
    }
}
