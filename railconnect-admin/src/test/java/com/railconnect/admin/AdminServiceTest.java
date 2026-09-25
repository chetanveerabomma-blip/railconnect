package com.railconnect.admin;

import com.railconnect.admin.dto.DashboardStatsDto;
import com.railconnect.admin.dto.VerifyTicketRequest;
import com.railconnect.admin.model.TicketVerification;
import com.railconnect.admin.repository.TicketVerificationRepository;
import com.railconnect.admin.service.AdminService;
import com.railconnect.auth.model.User;
import com.railconnect.auth.repository.UserRepository;
import com.railconnect.berthexchange.repository.AuditLogRepository;
import com.railconnect.berthexchange.repository.ExchangeRepository;
import com.railconnect.booking.model.Booking;
import com.railconnect.booking.model.Train;
import com.railconnect.booking.repository.*;
import com.railconnect.seatengine.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AdminServiceTest {

    private TrainRepository trainRepository;
    private StationRepository stationRepository;
    private TrainRouteRepository trainRouteRepository;
    private BookingRepository bookingRepository;
    private FareRuleRepository fareRuleRepository;
    private ExchangeRepository exchangeRepository;
    private UserRepository userRepository;
    private SeatRepository seatRepository;
    private TicketVerificationRepository verificationRepository;
    private AuditLogRepository auditLogRepository;
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        trainRepository = mock(TrainRepository.class);
        stationRepository = mock(StationRepository.class);
        trainRouteRepository = mock(TrainRouteRepository.class);
        bookingRepository = mock(BookingRepository.class);
        fareRuleRepository = mock(FareRuleRepository.class);
        exchangeRepository = mock(ExchangeRepository.class);
        userRepository = mock(UserRepository.class);
        seatRepository = mock(SeatRepository.class);
        verificationRepository = mock(TicketVerificationRepository.class);
        auditLogRepository = mock(AuditLogRepository.class);

        adminService = new AdminService(
                trainRepository, stationRepository, trainRouteRepository, bookingRepository,
                fareRuleRepository, exchangeRepository, userRepository,
                seatRepository, verificationRepository, auditLogRepository
        );
    }

    @Test
    void testDashboardStats() {
        when(trainRepository.count()).thenReturn(5L);
        when(stationRepository.count()).thenReturn(10L);
        when(bookingRepository.findAll()).thenReturn(Collections.emptyList());

        DashboardStatsDto stats = adminService.getDashboardStats();
        assertNotNull(stats);
        assertEquals(5, stats.getTotalTrains());
        assertEquals(10, stats.getTotalStations());
        assertNotNull(stats.getTrainOccupancy());
    }

    @Test
    void testVerifyTicketByPNR() {
        User inspector = new User("inspector_anand", "inspector@railconnect.com", "9988776611", "hash", "ROLE_INSPECTOR", "Anand", "M");
        inspector.setId(2L);

        Train train = new Train();
        train.setTrainNumber("12638");

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setPnrNumber("4827193056");
        booking.setTrain(train);
        booking.setJourneyDate(LocalDate.now().plusDays(1));

        when(userRepository.findByUsername("inspector_anand")).thenReturn(Optional.of(inspector));
        when(bookingRepository.findByPnrNumber("4827193056")).thenReturn(Optional.of(booking));

        TicketVerification tv = new TicketVerification(booking, inspector, null, "VERIFIED", "Valid ID");
        when(verificationRepository.save(any(TicketVerification.class))).thenReturn(tv);

        VerifyTicketRequest req = new VerifyTicketRequest("4827193056", null, "VERIFIED", "Valid ID");
        TicketVerification result = adminService.verifyTicket(req, "inspector_anand");

        assertNotNull(result);
        assertEquals("VERIFIED", result.getVerificationStatus());
        assertEquals("4827193056", result.getBooking().getPnrNumber());
    }

    @Test
    void testGetAllTrainsAndFares() {
        when(trainRepository.findAll()).thenReturn(Collections.emptyList());
        when(fareRuleRepository.findAll()).thenReturn(Collections.emptyList());

        assertNotNull(adminService.getAllTrains());
        assertNotNull(adminService.getAllFares());
    }
}
