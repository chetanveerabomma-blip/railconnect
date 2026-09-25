package com.railconnect.berthexchange;

import com.railconnect.berthexchange.model.ExchangeStatus;
import com.railconnect.berthexchange.repository.AuditLogRepository;
import com.railconnect.berthexchange.repository.ExchangeRepository;
import com.railconnect.berthexchange.service.BerthExchangeService;
import com.railconnect.berthexchange.service.ExchangeRuleEngine;
import com.railconnect.booking.model.*;
import com.railconnect.booking.repository.BookingPassengerRepository;
import com.railconnect.booking.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BerthExchangeTest {

    private ExchangeRepository exchangeRepository;
    private AuditLogRepository auditLogRepository;
    private BookingRepository bookingRepository;
    private BookingPassengerRepository passengerRepository;
    private ExchangeRuleEngine ruleEngine;
    private BerthExchangeService service;

    @BeforeEach
    void setUp() {
        exchangeRepository = mock(ExchangeRepository.class);
        auditLogRepository = mock(AuditLogRepository.class);
        bookingRepository = mock(BookingRepository.class);
        passengerRepository = mock(BookingPassengerRepository.class);
        ruleEngine = new ExchangeRuleEngine(exchangeRepository);
        service = new BerthExchangeService(exchangeRepository, auditLogRepository, bookingRepository, passengerRepository, ruleEngine);
    }

    @Test
    void testRuleValidationSameTrainAndConfirmed() {
        Train trainA = new Train();
        trainA.setId(1L);
        trainA.setTrainNumber("12638");

        Train trainB = new Train();
        trainB.setId(2L);
        trainB.setTrainNumber("20608");

        Booking b1 = new Booking();
        b1.setId(1L);
        b1.setTrain(trainA);
        b1.setStatus(BookingStatus.CONFIRMED);
        b1.setJourneyDate(LocalDate.now().plusDays(2));
        b1.setCoachType("3A");

        BookingPassenger p1 = new BookingPassenger("Rahul", 29, "MALE", "LOWER");
        p1.setId(1L);
        p1.setStatus(BookingStatus.CONFIRMED);

        Booking b2 = new Booking();
        b2.setId(2L);
        b2.setTrain(trainB); // Different train!
        b2.setStatus(BookingStatus.CONFIRMED);
        b2.setJourneyDate(LocalDate.now().plusDays(2));
        b2.setCoachType("3A");

        BookingPassenger p2 = new BookingPassenger("Priya", 27, "FEMALE", "UPPER");
        p2.setId(2L);
        p2.setStatus(BookingStatus.CONFIRMED);

        when(exchangeRepository.findActiveRequestsForPassenger(anyLong())).thenReturn(Collections.emptyList());

        // Violates Rule 2 (different train)
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            ruleEngine.validateExchangeEligibility(b1, p1, b2, p2);
        });

        assertTrue(ex.getMessage().contains("Rule 2 Violation"));
    }
}
