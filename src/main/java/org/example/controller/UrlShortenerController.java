package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ShortenUrlRequest;
import org.example.dto.ShortenUrlResponse;
import org.example.service.UrlShortenerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping("/api/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request) {
        try {
            log.info("Received request to shorten URL: {}", request.getUrl());
            ShortenUrlResponse response = urlShortenerService.shortenUrl(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error shortening URL: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/s/{shortUrl}")
    public RedirectView redirectToLongUrl(@PathVariable String shortUrl) {
        log.info("Received request to redirect short URL: {}", shortUrl);
        String longUrl = urlShortenerService.getLongUrl(shortUrl);

        if (longUrl != null) {
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl(longUrl);
            return redirectView;
        } else {
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/error.html");
            return redirectView;
        }
    }

    @GetMapping("/api/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("URL Shortener Service is running!");
    }
}

