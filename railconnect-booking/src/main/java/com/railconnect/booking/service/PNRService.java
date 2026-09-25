package com.railconnect.booking.service;

import com.railconnect.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class PNRService {

    private final BookingRepository bookingRepository;
    private final SecureRandom random = new SecureRandom();

    public PNRService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Generates a 10-digit unique PNR.
     * Guaranteed collision-free by database uniqueness verification.
     * Example: 4827193056
     */
    public synchronized String generateUniquePNR() {
        int maxAttempts = 100;
        for (int i = 0; i < maxAttempts; i++) {
            // First digit 2-9 (standard Indian Railways PNR format)
            int firstDigit = 2 + random.nextInt(8);
            long remainingDigits = (long) (random.nextDouble() * 1_000_000_000L);
            String pnr = String.format("%d%09d", firstDigit, remainingDigits);

            if (!bookingRepository.existsByPnrNumber(pnr)) {
                return pnr;
            }
        }
        throw new IllegalStateException("Failed to generate a unique 10-digit PNR after " + maxAttempts + " attempts.");
    }
}
