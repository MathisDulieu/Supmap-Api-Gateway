package com.novus.api_gateway.service;

import com.novus.api_gateway.configuration.EnvConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class QRcodeService {

    private final EnvConfiguration envConfiguration;

    public String buildQRCodeUrl(String data, int size) {
        try {
            String encodedData = URLEncoder.encode(data, StandardCharsets.UTF_8);

            return UriComponentsBuilder.fromUriString(envConfiguration.getQrCodeApiUrl())
                    .queryParam("data", encodedData)
                    .queryParam("size", size + "x" + size)
                    .queryParam("format", "png")
                    .build()
                    .toUriString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code URL. Please try again or contact support if the issue persists.", e);
        }
    }
}