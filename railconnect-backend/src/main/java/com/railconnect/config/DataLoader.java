package com.railconnect.config;

import com.railconnect.auth.model.User;
import com.railconnect.auth.repository.UserRepository;
import com.railconnect.berthexchange.model.BerthExchangeRequest;
import com.railconnect.berthexchange.model.ExchangeStatus;
import com.railconnect.berthexchange.repository.ExchangeRepository;
import com.railconnect.booking.model.*;
import com.railconnect.booking.repository.*;
import com.railconnect.seatengine.model.BerthType;
import com.railconnect.seatengine.model.Coach;
import com.railconnect.seatengine.model.Seat;
import com.railconnect.seatengine.repository.CoachRepository;
import com.railconnect.seatengine.repository.SeatRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final TrainRepository trainRepository;
    private final TrainRouteRepository trainRouteRepository;
    private final CoachRepository coachRepository;
    private final SeatRepository seatRepository;
    private final FareRuleRepository fareRuleRepository;
    private final BookingRepository bookingRepository;
    private final BookingPassengerRepository passengerRepository;
    private final PaymentRepository paymentRepository;
    private final ExchangeRepository exchangeRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository,
                      StationRepository stationRepository,
                      TrainRepository trainRepository,
                      TrainRouteRepository trainRouteRepository,
                      CoachRepository coachRepository,
                      SeatRepository seatRepository,
                      FareRuleRepository fareRuleRepository,
                      BookingRepository bookingRepository,
                      BookingPassengerRepository passengerRepository,
                      PaymentRepository paymentRepository,
                      ExchangeRepository exchangeRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.stationRepository = stationRepository;
        this.trainRepository = trainRepository;
        this.trainRouteRepository = trainRouteRepository;
        this.coachRepository = coachRepository;
        this.seatRepository = seatRepository;
        this.fareRuleRepository = fareRuleRepository;
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.paymentRepository = paymentRepository;
        this.exchangeRepository = exchangeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (stationRepository.count() > 0) {
            return; // Data already initialized
        }

        System.out.println(">>> RailConnect: Bootstrapping master railway reference data & realistic scenarios...");

        // 1. Users
        String encodedPass = passwordEncoder.encode("password123");
        User admin = userRepository.save(new User("admin", "admin@railconnect.com", "9876543299", encodedPass, "ROLE_ADMIN", "Rajesh", "Verma"));
        User inspector = userRepository.save(new User("inspector_anand", "inspector@railconnect.com", "9876543288", encodedPass, "ROLE_INSPECTOR", "Anand", "Mohan"));
        User rahul = userRepository.save(new User("rahul_sharma", "rahul@railconnect.com", "9876543210", encodedPass, "ROLE_PASSENGER", "Rahul", "Sharma"));
        User priya = userRepository.save(new User("priya_patel", "priya@railconnect.com", "9876543211", encodedPass, "ROLE_PASSENGER", "Priya", "Patel"));
        User suresh = userRepository.save(new User("suresh_kumar", "suresh@railconnect.com", "9876543212", encodedPass, "ROLE_PASSENGER", "Suresh", "Kumar"));

        // 2. Stations
        Station ndls = stationRepository.save(new Station("NDLS", "New Delhi Railway Station", "New Delhi", "Delhi", "NR", 16));
        Station bct = stationRepository.save(new Station("BCT", "Mumbai Central", "Mumbai", "Maharashtra", "WR", 8));
        Station mas = stationRepository.save(new Station("MAS", "Chennai Central", "Chennai", "Tamil Nadu", "SR", 12));
        Station ms = stationRepository.save(new Station("MS", "Chennai Egmore", "Chennai", "Tamil Nadu", "SR", 11));
        Station tpj = stationRepository.save(new Station("TPJ", "Tiruchchirappalli Junction (Trichy)", "Tiruchirappalli", "Tamil Nadu", "SR", 8));
        Station sbc = stationRepository.save(new Station("SBC", "KSR Bengaluru City Junction", "Bengaluru", "Karnataka", "SWR", 10));
        Station kpd = stationRepository.save(new Station("KPD", "Katpadi Junction", "Vellore", "Tamil Nadu", "SR", 5));
        Station hwh = stationRepository.save(new Station("HWH", "Howrah Junction", "Kolkata", "West Bengal", "ER", 23));
        Station hyb = stationRepository.save(new Station("HYB", "Hyderabad Deccan", "Hyderabad", "Telangana", "SCR", 6));
        Station adi = stationRepository.save(new Station("ADI", "Ahmedabad Junction", "Ahmedabad", "Gujarat", "WR", 12));
        Station cnb = stationRepository.save(new Station("CNB", "Kanpur Central", "Kanpur", "Uttar Pradesh", "NCR", 10));

        // 3. Fare Rules
        fareRuleRepository.save(new FareRule("EXPRESS", "SL", 0.45, 20.0, 30.0, 0.0));
        fareRuleRepository.save(new FareRule("EXPRESS", "3A", 1.25, 40.0, 45.0, 5.0));
        fareRuleRepository.save(new FareRule("EXPRESS", "2A", 1.80, 50.0, 45.0, 5.0));
        fareRuleRepository.save(new FareRule("EXPRESS", "1A", 2.80, 60.0, 75.0, 5.0));
        fareRuleRepository.save(new FareRule("EXPRESS", "CC", 0.95, 40.0, 45.0, 5.0));
        fareRuleRepository.save(new FareRule("VANDE_BHARAT", "CC", 1.45, 40.0, 75.0, 5.0));
        fareRuleRepository.save(new FareRule("VANDE_BHARAT", "EC", 2.65, 60.0, 75.0, 5.0));
        fareRuleRepository.save(new FareRule("RAJDHANI", "3A", 1.45, 40.0, 75.0, 5.0));
        fareRuleRepository.save(new FareRule("RAJDHANI", "2A", 2.10, 50.0, 75.0, 5.0));
        fareRuleRepository.save(new FareRule("RAJDHANI", "1A", 3.10, 60.0, 75.0, 5.0));

        // 4. Trains
        // Train 1: Vande Bharat (SBC -> MAS)
        Train vb = new Train();
        vb.setTrainNumber("20608");
        vb.setTrainName("Vande Bharat Express");
        vb.setTrainType("VANDE_BHARAT");
        vb.setSourceStation(sbc);
        vb.setDestinationStation(mas);
        vb.setDepartureTime(LocalTime.of(5, 45));
        vb.setArrivalTime(LocalTime.of(10, 10));
        vb.setDurationHours(4.42);
        vb.setRunningDays("MON,TUE,WED,THU,FRI,SUN");
        vb = trainRepository.save(vb);

        // Train 2: Pandian Express (TPJ -> MAS & MS)
        Train pandian = new Train();
        pandian.setTrainNumber("12638");
        pandian.setTrainName("Pandian Superfast Express");
        pandian.setTrainType("EXPRESS");
        pandian.setSourceStation(tpj);
        pandian.setDestinationStation(mas);
        pandian.setDepartureTime(LocalTime.of(21, 35));
        pandian.setArrivalTime(LocalTime.of(5, 15));
        pandian.setDurationHours(7.67);
        pandian.setRunningDays("MON,TUE,WED,THU,FRI,SAT,SUN");
        pandian = trainRepository.save(pandian);

        // Train 3: Mumbai Rajdhani (NDLS -> BCT)
        Train rajdhani = new Train();
        rajdhani.setTrainNumber("12952");
        rajdhani.setTrainName("Mumbai Rajdhani Express");
        rajdhani.setTrainType("RAJDHANI");
        rajdhani.setSourceStation(ndls);
        rajdhani.setDestinationStation(bct);
        rajdhani.setDepartureTime(LocalTime.of(16, 55));
        rajdhani.setArrivalTime(LocalTime.of(8, 35));
        rajdhani.setDurationHours(15.67);
        rajdhani.setRunningDays("MON,TUE,WED,THU,FRI,SAT,SUN");
        rajdhani = trainRepository.save(rajdhani);

        // 5. Train Routes
        // 20608 Routes (SBC -> KPD -> MAS)
        TrainRoute vbR1 = new TrainRoute(); vbR1.setTrain(vb); vbR1.setStation(sbc); vbR1.setStopSequence(1); vbR1.setDistanceFromSourceKm(0.0); vbR1.setDepartureTime(LocalTime.of(5, 45));
        TrainRoute vbR2 = new TrainRoute(); vbR2.setTrain(vb); vbR2.setStation(kpd); vbR2.setStopSequence(2); vbR2.setDistanceFromSourceKm(229.0); vbR2.setArrivalTime(LocalTime.of(8, 30)); vbR2.setDepartureTime(LocalTime.of(8, 32));
        TrainRoute vbR3 = new TrainRoute(); vbR3.setTrain(vb); vbR3.setStation(mas); vbR3.setStopSequence(3); vbR3.setDistanceFromSourceKm(359.0); vbR3.setArrivalTime(LocalTime.of(10, 10));
        trainRouteRepository.saveAll(List.of(vbR1, vbR2, vbR3));

        // 12638 Routes (TPJ -> MS -> MAS)
        TrainRoute pR1 = new TrainRoute(); pR1.setTrain(pandian); pR1.setStation(tpj); pR1.setStopSequence(1); pR1.setDistanceFromSourceKm(0.0); pR1.setDepartureTime(LocalTime.of(21, 35));
        TrainRoute pR2 = new TrainRoute(); pR2.setTrain(pandian); pR2.setStation(ms); pR2.setStopSequence(2); pR2.setDistanceFromSourceKm(336.0); pR2.setArrivalTime(LocalTime.of(4, 55)); pR2.setDepartureTime(LocalTime.of(5, 0));
        TrainRoute pR3 = new TrainRoute(); pR3.setTrain(pandian); pR3.setStation(mas); pR3.setStopSequence(3); pR3.setDistanceFromSourceKm(340.0); pR3.setArrivalTime(LocalTime.of(5, 15));
        trainRouteRepository.saveAll(List.of(pR1, pR2, pR3));

        // 12952 Routes (NDLS -> CNB -> ADI -> BCT)
        TrainRoute rR1 = new TrainRoute(); rR1.setTrain(rajdhani); rR1.setStation(ndls); rR1.setStopSequence(1); rR1.setDistanceFromSourceKm(0.0); rR1.setDepartureTime(LocalTime.of(16, 55));
        TrainRoute rR2 = new TrainRoute(); rR2.setTrain(rajdhani); rR2.setStation(cnb); rR2.setStopSequence(2); rR2.setDistanceFromSourceKm(440.0); rR2.setArrivalTime(LocalTime.of(21, 30)); rR2.setDepartureTime(LocalTime.of(21, 35));
        TrainRoute rR3 = new TrainRoute(); rR3.setTrain(rajdhani); rR3.setStation(adi); rR3.setStopSequence(3); rR3.setDistanceFromSourceKm(935.0); rR3.setArrivalTime(LocalTime.of(3, 15)); rR3.setDepartureTime(LocalTime.of(3, 25));
        TrainRoute rR4 = new TrainRoute(); rR4.setTrain(rajdhani); rR4.setStation(bct); rR4.setStopSequence(4); rR4.setDistanceFromSourceKm(1384.0); rR4.setArrivalTime(LocalTime.of(8, 35));
        trainRouteRepository.saveAll(List.of(rR1, rR2, rR3, rR4));

        // 6. Coaches & Seats
        // Vande Bharat
        Coach c1 = coachRepository.save(new Coach(vb.getId(), "C1", "CC", 78));
        Coach c2 = coachRepository.save(new Coach(vb.getId(), "C2", "CC", 78));
        Coach e1 = coachRepository.save(new Coach(vb.getId(), "E1", "EC", 52));
        // Pandian
        Coach b1 = coachRepository.save(new Coach(pandian.getId(), "B1", "3A", 64));
        Coach b2 = coachRepository.save(new Coach(pandian.getId(), "B2", "3A", 64));
        Coach s1 = coachRepository.save(new Coach(pandian.getId(), "S1", "SL", 72));
        // Rajdhani
        Coach rA1 = coachRepository.save(new Coach(rajdhani.getId(), "A1", "2A", 54));
        Coach rB1 = coachRepository.save(new Coach(rajdhani.getId(), "B1", "3A", 64));
        Coach rH1 = coachRepository.save(new Coach(rajdhani.getId(), "H1", "1A", 24));

        // Populate Seats for B1 & rB1 (3A layout: LOWER, MIDDLE, UPPER, SIDE_LOWER, SIDE_UPPER)
        BerthType[] sleeperTypes = {BerthType.LOWER, BerthType.MIDDLE, BerthType.UPPER, BerthType.LOWER, BerthType.MIDDLE, BerthType.UPPER, BerthType.SIDE_LOWER, BerthType.SIDE_UPPER};
        for (int i = 1; i <= 64; i++) {
            BerthType type = sleeperTypes[(i - 1) % 8];
            int cabin = ((i - 1) / 8) + 1;
            seatRepository.save(new Seat(b1, i, type, cabin));
            seatRepository.save(new Seat(rB1, i, type, cabin));
        }

        // Populate Seats for C1, C2 (CC layout: WINDOW, MIDDLE, AISLE)
        BerthType[] ccTypes = {BerthType.WINDOW, BerthType.MIDDLE, BerthType.AISLE, BerthType.AISLE, BerthType.WINDOW};
        for (int i = 1; i <= 50; i++) {
            BerthType type = ccTypes[(i - 1) % 5];
            int cabin = ((i - 1) / 5) + 1;
            seatRepository.save(new Seat(c1, i, type, cabin));
            seatRepository.save(new Seat(c2, i, type, cabin));
        }

        // Populate Seats for E1 (EC layout: WINDOW, AISLE, WINDOW)
        BerthType[] ecTypes = {BerthType.WINDOW, BerthType.AISLE, BerthType.AISLE, BerthType.WINDOW};
        for (int i = 1; i <= 40; i++) {
            BerthType type = ecTypes[(i - 1) % 4];
            int cabin = ((i - 1) / 4) + 1;
            seatRepository.save(new Seat(e1, i, type, cabin));
        }

        // Populate Seats for rA1 (2A) & rH1 (1A)
        for (int i = 1; i <= 40; i++) {
            BerthType type = (i % 2 == 1) ? BerthType.LOWER : BerthType.UPPER;
            seatRepository.save(new Seat(rA1, i, type, (i / 4) + 1));
        }
        for (int i = 1; i <= 20; i++) {
            BerthType type = (i % 2 == 1) ? BerthType.LOWER : BerthType.UPPER;
            seatRepository.save(new Seat(rH1, i, type, (i / 2) + 1));
        }

        // 7. Demo Bookings with unique 10-digit PNR
        Booking booking1 = new Booking();
        booking1.setPnrNumber("4827193056");
        booking1.setUser(rahul);
        booking1.setTrain(pandian);
        booking1.setFromStation(tpj);
        booking1.setToStation(mas);
        booking1.setJourneyDate(LocalDate.now().plusDays(2));
        booking1.setCoachType("3A");
        booking1.setBaseFare(420.0);
        booking1.setReservationCharge(40.0);
        booking1.setServiceCharge(45.0);
        booking1.setTaxAmount(25.0);
        booking1.setDiscountAmount(0.0);
        booking1.setTotalFare(530.0);
        booking1.setStatus(BookingStatus.CONFIRMED);
        Booking savedB1 = bookingRepository.save(booking1);

        BookingPassenger pRahul = new BookingPassenger("Rahul Sharma", 29, "MALE", "LOWER");
        pRahul.setBooking(savedB1);
        pRahul.setSeatId(1L);
        pRahul.setAllocatedCoach("B1");
        pRahul.setAllocatedSeatNumber(1);
        pRahul.setAllocatedBerthType("LOWER");
        pRahul.setStatus(BookingStatus.CONFIRMED);
        passengerRepository.save(pRahul);

        paymentRepository.save(new Payment(savedB1, "TXN-RAIL-2026-001", "UPI", 530.0, "SUCCESS"));

        // Booking 2: Priya Patel on same train & date (Eligible for exchange!)
        Booking booking2 = new Booking();
        booking2.setPnrNumber("8192304851");
        booking2.setUser(priya);
        booking2.setTrain(pandian);
        booking2.setFromStation(tpj);
        booking2.setToStation(mas);
        booking2.setJourneyDate(LocalDate.now().plusDays(2));
        booking2.setCoachType("3A");
        booking2.setBaseFare(420.0);
        booking2.setReservationCharge(40.0);
        booking2.setServiceCharge(45.0);
        booking2.setTaxAmount(25.0);
        booking2.setDiscountAmount(0.0);
        booking2.setTotalFare(530.0);
        booking2.setStatus(BookingStatus.CONFIRMED);
        Booking savedB2 = bookingRepository.save(booking2);

        BookingPassenger pPriya = new BookingPassenger("Priya Patel", 27, "FEMALE", "UPPER");
        pPriya.setBooking(savedB2);
        pPriya.setSeatId(3L);
        pPriya.setAllocatedCoach("B1");
        pPriya.setAllocatedSeatNumber(3);
        pPriya.setAllocatedBerthType("UPPER");
        pPriya.setStatus(BookingStatus.CONFIRMED);
        passengerRepository.save(pPriya);

        paymentRepository.save(new Payment(savedB2, "TXN-RAIL-2026-002", "CREDIT_CARD", 530.0, "SUCCESS"));

        // 8. Demo Berth Exchange Request
        BerthExchangeRequest req = new BerthExchangeRequest();
        req.setRequesterBooking(savedB2);
        req.setRequesterPassenger(pPriya);
        req.setTargetBooking(savedB1);
        req.setTargetPassenger(pRahul);
        req.setTrain(pandian);
        req.setJourneyDate(LocalDate.now().plusDays(2));
        req.setStatus(ExchangeStatus.REQUESTED);
        req.setRequestReason("Prefers lower berth due to slight knee sprain. Both passengers in Coach B1.");
        req.setCreatedAt(LocalDateTime.now().minusHours(3));
        req.setUpdatedAt(LocalDateTime.now().minusHours(3));
        exchangeRepository.save(req);

        System.out.println(">>> RailConnect: Successfully populated master trains, stations, coaches, seats, and demo scenarios!");
    }
}
