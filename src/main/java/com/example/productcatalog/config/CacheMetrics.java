package com.example.productcatalog.config;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CacheMetrics implements MeterBinder {

    private final CacheManager cacheManager;

    public CacheMetrics(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        cacheManager.getCacheNames().forEach(cacheName -> {
            CaffeineCache cache = (CaffeineCache) Objects.requireNonNull(
                    cacheManager.getCache(cacheName));
            com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache =
                    cache.getNativeCache();

            Gauge.builder("cache.size", nativeCache, c -> c.estimatedSize())
                    .tag("cache", cacheName)
                    .description("The approximate number of entries in this cache")
                    .register(registry);

            Gauge.builder("cache.hit.count", nativeCache, c -> c.stats().hitCount())
                    .tag("cache", cacheName)
                    .description("The number of times cache lookup methods have returned a cached value")
                    .register(registry);

            Gauge.builder("cache.miss.count", nativeCache, c -> c.stats().missCount())
                    .tag("cache", cacheName)
                    .description("The number of times cache lookup methods have returned an uncached value")
                    .register(registry);

            Gauge.builder("cache.hit.ratio", nativeCache,
                            c -> {
                                CacheStats stats = c.stats();
                                return stats.hitRate();
                            })
                    .tag("cache", cacheName)
                    .description("The ratio of hits to requests")
                    .register(registry);
        });
    }
}