package com.railconnect.booking.service;

import com.railconnect.auth.model.User;
import com.railconnect.auth.repository.UserRepository;
import com.railconnect.booking.dto.BookingRequest;
import com.railconnect.booking.dto.BookingResponse;
import com.railconnect.booking.dto.FareCalculationDto;
import com.railconnect.booking.model.*;
import com.railconnect.booking.repository.*;
import com.railconnect.seatengine.dto.AutoAllocateRequest;
import com.railconnect.seatengine.dto.AutoAllocateResponse;
import com.railconnect.seatengine.model.Seat;
import com.railconnect.seatengine.repository.SeatRepository;
import com.railconnect.seatengine.service.SeatAllocationService;
import com.railconnect.seatengine.service.SeatLockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingPassengerRepository passengerRepository;
    private final PaymentRepository paymentRepository;
    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final TrainRouteRepository trainRouteRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final PNRService pnrService;
    private final FareCalculationService fareCalculationService;
    private final SeatLockService seatLockService;
    private final SeatAllocationService seatAllocationService;

    public BookingService(BookingRepository bookingRepository,
                          BookingPassengerRepository passengerRepository,
                          PaymentRepository paymentRepository,
                          TrainRepository trainRepository,
                          StationRepository stationRepository,
                          TrainRouteRepository trainRouteRepository,
                          UserRepository userRepository,
                          SeatRepository seatRepository,
                          PNRService pnrService,
                          FareCalculationService fareCalculationService,
                          SeatLockService seatLockService,
                          SeatAllocationService seatAllocationService) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.paymentRepository = paymentRepository;
        this.trainRepository = trainRepository;
        this.stationRepository = stationRepository;
        this.trainRouteRepository = trainRouteRepository;
        this.userRepository = userRepository;
        this.seatRepository = seatRepository;
        this.pnrService = pnrService;
        this.fareCalculationService = fareCalculationService;
        this.seatLockService = seatLockService;
        this.seatAllocationService = seatAllocationService;
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        Train train = trainRepository.findById(request.getTrainId())
                .orElseThrow(() -> new IllegalArgumentException("Train not found with ID: " + request.getTrainId()));

        Station fromStation = stationRepository.findByCodeIgnoreCase(request.getFromStationCode())
                .orElseThrow(() -> new IllegalArgumentException("Invalid from-station: " + request.getFromStationCode()));

        Station toStation = stationRepository.findByCodeIgnoreCase(request.getToStationCode())
                .orElseThrow(() -> new IllegalArgumentException("Invalid to-station: " + request.getToStationCode()));

        LocalDate journeyDate = LocalDate.parse(request.getJourneyDate());
        if (journeyDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Journey date cannot be in the past.");
        }

        // Calculate segment distance
        TrainRoute fromRoute = trainRouteRepository.findByTrainIdAndStationId(train.getId(), fromStation.getId()).orElse(null);
        TrainRoute toRoute = trainRouteRepository.findByTrainIdAndStationId(train.getId(), toStation.getId()).orElse(null);

        double distance = 300.0;
        if (fromRoute != null && toRoute != null) {
            distance = Math.max(50.0, toRoute.getDistanceFromSourceKm() - fromRoute.getDistanceFromSourceKm());
        }

        int passengerCount = request.getPassengers().size();
        FareCalculationDto fare = fareCalculationService.calculateFare(
                train.getTrainType(),
                request.getCoachType(),
                distance,
                passengerCount,
                request.getDiscountAmount()
        );

        // Generate unique 10-digit PNR
        String pnr = pnrService.generateUniquePNR();

        // Create Booking entity
        Booking booking = new Booking();
        booking.setPnrNumber(pnr);
        booking.setUser(user);
        booking.setTrain(train);
        booking.setFromStation(fromStation);
        booking.setToStation(toStation);
        booking.setJourneyDate(journeyDate);
        booking.setBookingDate(LocalDateTime.now());
        booking.setCoachType(request.getCoachType());
        booking.setBaseFare(fare.getBaseFare());
        booking.setReservationCharge(fare.getReservationCharge());
        booking.setServiceCharge(fare.getServiceCharge());
        booking.setTaxAmount(fare.getTaxAmount());
        booking.setDiscountAmount(fare.getDiscountAmount());
        booking.setTotalFare(fare.getFinalFare());
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking savedBooking = bookingRepository.save(booking);

        // Allocate Seats
        Set<Long> bookedSeatIds = bookingRepository.findBookedSeatIds(train.getId(), journeyDate);
        List<BookingResponse.PassengerTicketInfoDto> passengerTicketInfos = new ArrayList<>();

        for (BookingRequest.PassengerDetailDto pDto : request.getPassengers()) {
            BookingPassenger bp = new BookingPassenger(
                    pDto.getName(),
                    pDto.getAge(),
                    pDto.getGender(),
                    pDto.getBerthPreference()
            );
            bp.setBooking(savedBooking);

            Seat allocatedSeat = null;
            if (pDto.getSelectedSeatId() != null) {
                allocatedSeat = seatRepository.findById(pDto.getSelectedSeatId()).orElse(null);
            }

            // If no manual seat picked, use auto-allocation for this passenger
            if (allocatedSeat == null) {
                AutoAllocateRequest autoReq = new AutoAllocateRequest();
                autoReq.setTrainId(train.getId());
                autoReq.setCoachType(request.getCoachType());
                autoReq.setPassengers(List.of(new AutoAllocateRequest.PassengerPreference(
                        pDto.getName(), pDto.getAge(), pDto.getGender(), pDto.getBerthPreference()
                )));

                AutoAllocateResponse autoResp = seatAllocationService.autoAllocateSeats(autoReq, bookedSeatIds, user.getId());
                if (autoResp.isSuccessful() && !autoResp.getAllocations().isEmpty()) {
                    Long allocatedId = autoResp.getAllocations().get(0).getSeatId();
                    allocatedSeat = seatRepository.findById(allocatedId).orElse(null);
                }
            }

            if (allocatedSeat != null) {
                bp.setSeatId(allocatedSeat.getId());
                bp.setAllocatedCoach(allocatedSeat.getCoach().getCoachNumber());
                bp.setAllocatedSeatNumber(allocatedSeat.getSeatNumber());
                bp.setAllocatedBerthType(allocatedSeat.getBerthType().name());
                bp.setStatus(BookingStatus.CONFIRMED);

                // Release temporary lock and lock as BOOKED
                seatLockService.confirmSeat(allocatedSeat.getId());
                bookedSeatIds.add(allocatedSeat.getId());
            } else {
                bp.setStatus(BookingStatus.RAC);
                bp.setAllocatedCoach("RAC");
                bp.setAllocatedSeatNumber(0);
                bp.setAllocatedBerthType("RAC");
            }

            BookingPassenger savedBp = passengerRepository.save(bp);

            passengerTicketInfos.add(new BookingResponse.PassengerTicketInfoDto(
                    savedBp.getId(),
                    savedBp.getPassengerName(),
                    savedBp.getPassengerAge(),
                    savedBp.getPassengerGender(),
                    savedBp.getAllocatedCoach(),
                    savedBp.getAllocatedSeatNumber(),
                    savedBp.getAllocatedBerthType(),
                    savedBp.getStatus()
            ));
        }

        // Record simulated payment
        String txnId = "TXN-RAIL-" + System.currentTimeMillis();
        Payment payment = new Payment(savedBooking, txnId, request.getPaymentMethod(), fare.getFinalFare(), "SUCCESS");
        paymentRepository.save(payment);

        // Generate Digital Travel Pass QR payload
        String ticketId = "TKT-" + pnr.substring(0, 4) + "-" + savedBooking.getId();
        String qrPayload = String.format("RAILCONNECT|PNR:%s|TICKET:%s|TRAIN:%s|FROM:%s|TO:%s|DATE:%s|TOTAL:INR %.2f",
                pnr, ticketId, train.getTrainNumber(), fromStation.getCode(), toStation.getCode(), journeyDate, fare.getFinalFare());

        BookingResponse response = new BookingResponse();
        response.setBookingId(savedBooking.getId());
        response.setPnrNumber(pnr);
        response.setTicketId(ticketId);
        response.setTrainNumber(train.getTrainNumber());
        response.setTrainName(train.getTrainName());
        response.setFromStationName(fromStation.getName() + " (" + fromStation.getCode() + ")");
        response.setToStationName(toStation.getName() + " (" + toStation.getCode() + ")");
        response.setJourneyDate(journeyDate.toString());
        response.setCoachType(request.getCoachType());
        response.setTotalFare(fare.getFinalFare());
        response.setStatus(savedBooking.getStatus());
        response.setPaymentStatus("SUCCESS");
        response.setTransactionId(txnId);
        response.setQrCodeText(qrPayload);
        response.setPassengers(passengerTicketInfos);
        response.setMessage("Ticket reserved successfully! Your 10-digit PNR is " + pnr);

        return response;
    }

    public Optional<Booking> getBookingByPnr(String pnr) {
        return bookingRepository.findByPnrNumber(pnr);
    }

    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserIdOrderByBookingDateDesc(userId);
    }
}
