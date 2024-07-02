package com.festago.common.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Profile({"!test"})
@Slf4j
@Component
@RequiredArgsConstructor
public class CacheStatsLogger {

    private final CacheManager cacheManager;

    @EventListener(ContextClosedEvent.class)
    public void logCacheStats() {
        for (String cacheName : cacheManager.getCacheNames()) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache instanceof CaffeineCache caffeineCache) {
                log.info("CacheName={} CacheStats={}", cacheName, caffeineCache.getNativeCache().stats());
            }
        }
    }
}
