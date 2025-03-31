package com.novus.api_gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@EnableScheduling
public class RateLimitingService {

    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> blockExpirations = new ConcurrentHashMap<>();

    private static final int MAX_REQUESTS = 60;
    private static final long BLOCK_DURATION = 600000;

    public boolean isIpBlocked(String ipAddress) {
        return blockExpirations.containsKey(ipAddress) &&
                blockExpirations.get(ipAddress) > System.currentTimeMillis();
    }

    public boolean incrementAndCheckLimit(String ipAddress) {
        int count = requestCounts.getOrDefault(ipAddress, 0) + 1;
        requestCounts.put(ipAddress, count);
        return count > MAX_REQUESTS;
    }

    public void blockIpAddress(String ipAddress) {
        blockExpirations.put(ipAddress, System.currentTimeMillis() + BLOCK_DURATION);
    }

    public void resetRequestCounts() {
        requestCounts.clear();
        blockExpirations.clear();
    }

    @Scheduled(fixedRate = 60000)
    public void scheduledReset() {
        int blockedIpCount = blockExpirations.size();
        int requestCountSize = requestCounts.size();

        resetRequestCounts();

        log.info("🔄 Rate limit reset at: {} | {} IP(s) unblocked | {} request count(s) reset",
                System.currentTimeMillis(), blockedIpCount, requestCountSize);
    }

}
