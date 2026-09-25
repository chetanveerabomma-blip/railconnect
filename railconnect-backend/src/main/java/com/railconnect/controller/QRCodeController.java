package com.railconnect.controller;

import com.railconnect.service.QRCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*", maxAge = 3600)
public class QRCodeController {

    private final QRCodeService qrCodeService;

    public QRCodeController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/qr")
    public ResponseEntity<Map<String, String>> generateQR(
            @RequestParam(name = "text", defaultValue = "RAILCONNECT") String text,
            @RequestParam(name = "size", defaultValue = "200") int size) {
        String base64Image = qrCodeService.generateQRCodeBase64(text, size, size);
        return ResponseEntity.ok(Map.of("qrImage", base64Image, "payload", text));
    }
}
