package com.railconnect.booking;

import com.railconnect.booking.dto.FareCalculationDto;
import com.railconnect.booking.model.FareRule;
import com.railconnect.booking.repository.BookingRepository;
import com.railconnect.booking.repository.FareRuleRepository;
import com.railconnect.booking.service.FareCalculationService;
import com.railconnect.booking.service.PNRService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    private FareRuleRepository fareRuleRepository;
    private FareCalculationService fareCalculationService;
    private BookingRepository bookingRepository;
    private PNRService pnrService;

    @BeforeEach
    void setUp() {
        fareRuleRepository = mock(FareRuleRepository.class);
        fareCalculationService = new FareCalculationService(fareRuleRepository);
        bookingRepository = mock(BookingRepository.class);
        pnrService = new PNRService(bookingRepository);
    }

    @Test
    void testFareFormulaCalculation() {
        // Base Fare + Reservation Charge + Service Charge + Tax - Discount = Final Fare
        FareRule rule = new FareRule("EXPRESS", "3A", 1.25, 40.0, 45.0, 5.0);
        when(fareRuleRepository.findByTrainTypeAndCoachType("EXPRESS", "3A")).thenReturn(Optional.of(rule));

        // Distance 300 km, 1 passenger, 0 discount
        // Base = 300 * 1.25 = 375
        // Res = 40
        // Service = 45
        // Subtotal = 460
        // Tax (5%) = 23.0
        // Final = 483.0
        FareCalculationDto dto = fareCalculationService.calculateFare("EXPRESS", "3A", 300.0, 1, 0.0);

        assertEquals(375.0, dto.getBaseFare());
        assertEquals(40.0, dto.getReservationCharge());
        assertEquals(45.0, dto.getServiceCharge());
        assertEquals(23.0, dto.getTaxAmount());
        assertEquals(483.0, dto.getFinalFare());
    }

    @Test
    void testPNRGenerationFormatAndUniqueness() {
        when(bookingRepository.existsByPnrNumber(anyString())).thenReturn(false);

        String pnr = pnrService.generateUniquePNR();
        assertNotNull(pnr);
        assertEquals(10, pnr.length());
        assertTrue(Character.isDigit(pnr.charAt(0)));
        assertNotEquals('0', pnr.charAt(0)); // Cannot start with 0
    }
}
