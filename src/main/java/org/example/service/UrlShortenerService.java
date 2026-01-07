package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ShortenUrlRequest;
import org.example.dto.ShortenUrlResponse;
import org.example.entity.Url;
import org.example.repository.UrlRepository;
import org.example.util.Base62Encoder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UrlShortenerService {

    private final UrlRepository urlRepository;
    private final CounterRangeService counterRangeService;
    private final Base62Encoder base62Encoder;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String REDIS_KEY_PREFIX = "url:";
    private static final long REDIS_CACHE_TTL = 24; // hours

    @Transactional
    public ShortenUrlResponse shortenUrl(ShortenUrlRequest request) throws Exception {
        // Check if URL already exists
        Optional<Url> existingUrl = urlRepository.findByLongUrl(request.getUrl());
        if (existingUrl.isPresent()) {
            log.info("URL already exists: {}", request.getUrl());
            return mapToResponse(existingUrl.get());
        }

        // Generate unique counter and encode it
        long counter = counterRangeService.getNextCounter();
        String shortUrl = base62Encoder.encode(counter);

        // Calculate expiration date
        LocalDateTime expiresAt = null;
        if (request.getExpirationDays() != null && request.getExpirationDays() > 0) {
            expiresAt = LocalDateTime.now().plusDays(request.getExpirationDays());
        }

        // Save to database
        Url url = new Url();
        url.setShortUrl(shortUrl);
        url.setLongUrl(request.getUrl());
        url.setCounter(counter);
        url.setExpiresAt(expiresAt);
        url.setCreatedAt(LocalDateTime.now());

        urlRepository.save(url);

        // Cache in Redis
        cacheUrl(shortUrl, request.getUrl());

        log.info("Created short URL: {} for long URL: {}", shortUrl, request.getUrl());

        return mapToResponse(url);
    }

    public String getLongUrl(String shortUrl) {
        // Check Redis cache first
        String cachedUrl = getCachedUrl(shortUrl);
        if (cachedUrl != null) {
            log.info("Cache hit for short URL: {}", shortUrl);
            return cachedUrl;
        }

        // Check database
        Optional<Url> url = urlRepository.findByShortUrl(shortUrl);
        if (url.isPresent()) {
            Url urlEntity = url.get();

            // Check if expired
            if (urlEntity.getExpiresAt() != null && urlEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
                log.warn("URL expired: {}", shortUrl);
                return null;
            }

            // Cache for future requests
            cacheUrl(shortUrl, urlEntity.getLongUrl());

            return urlEntity.getLongUrl();
        }

        log.warn("Short URL not found: {}", shortUrl);
        return null;
    }

    private void cacheUrl(String shortUrl, String longUrl) {
        try {
            redisTemplate.opsForValue().set(
                REDIS_KEY_PREFIX + shortUrl,
                longUrl,
                REDIS_CACHE_TTL,
                TimeUnit.HOURS
            );
        } catch (Exception e) {
            log.error("Failed to cache URL in Redis: {}", e.getMessage());
        }
    }

    private String getCachedUrl(String shortUrl) {
        try {
            return redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + shortUrl);
        } catch (Exception e) {
            log.error("Failed to get cached URL from Redis: {}", e.getMessage());
            return null;
        }
    }

    private ShortenUrlResponse mapToResponse(Url url) {
        ShortenUrlResponse response = new ShortenUrlResponse();
        response.setShortUrl(url.getShortUrl());
        response.setLongUrl(url.getLongUrl());
        response.setCreatedAt(url.getCreatedAt());
        response.setExpiresAt(url.getExpiresAt());
        return response;
    }
}

