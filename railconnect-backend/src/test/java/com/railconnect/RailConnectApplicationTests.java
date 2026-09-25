package com.railconnect;

import com.railconnect.service.QRCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RailConnectApplicationTests {

    @Autowired
    private QRCodeService qrCodeService;

    @Test
    void contextLoads() {
        // Verifies complete Spring Boot context starts without errors
        assertNotNull(qrCodeService);
    }

    @Test
    void testQRCodeBase64Generation() {
        String payload = "RAILCONNECT|PNR:4827193056|TICKET:TKT-4827-1";
        String base64 = qrCodeService.generateQRCodeBase64(payload, 200, 200);

        assertNotNull(base64);
        assertTrue(base64.startsWith("data:image/png;base64,"));
        assertTrue(base64.length() > 100);
    }
}
