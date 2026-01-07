package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
@RequiredArgsConstructor
public class CounterRangeService {

    private final ZooKeeperService zooKeeperService;

    @Value("${counter.range.size}")
    private long rangeSize;

    private AtomicLong currentCounter = new AtomicLong(0);
    private long rangeEnd = 0;

    public synchronized long getNextCounter() throws Exception {
        if (currentCounter.get() >= rangeEnd) {
            allocateNewRange();
        }
        return currentCounter.getAndIncrement();
    }

    private void allocateNewRange() throws Exception {
        long rangeStart = zooKeeperService.getAndIncrementCounter(rangeSize);
        currentCounter.set(rangeStart);
        rangeEnd = rangeStart + rangeSize;
        log.info("Allocated new counter range: {} to {}", rangeStart, rangeEnd);
    }
}

