package com.example.productcatalog.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class CacheMetricsTest {

    @Test
    void bindTo_ShouldRegisterMetrics() {
        // Setup
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        CaffeineCache cache = new CaffeineCache("testCache",
                Caffeine.newBuilder().recordStats().build());
        cacheManager.setCaches(Collections.singletonList(cache));
        cacheManager.initializeCaches();

        MeterRegistry registry = new SimpleMeterRegistry();
        CacheMetrics cacheMetrics = new CacheMetrics(cacheManager);

        // Act
        cacheMetrics.bindTo(registry);

        // Assert
        assertNotNull(registry.find("cache.size").tags("cache", "testCache").gauge());
        assertNotNull(registry.find("cache.hit.count").tags("cache", "testCache").gauge());
        assertNotNull(registry.find("cache.miss.count").tags("cache", "testCache").gauge());
        assertNotNull(registry.find("cache.hit.ratio").tags("cache", "testCache").gauge());
    }
}