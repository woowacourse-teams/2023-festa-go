package com.festago.common.cache;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheInvalidator {

    private final CacheManager cacheManager;

    public void invalidate(String cacheName) {
        Optional.ofNullable(cacheManager.getCache(cacheName))
            .ifPresentOrElse(cache -> {
                cache.invalidate();
                log.info("{} 캐시를 초기화 했습니다.", cacheName);
            }, () -> log.error("{} 캐시를 찾을 수 없습니다.", cacheName));
    }
}
