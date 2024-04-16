package com.festago.school.application.v1;

import com.festago.common.cache.CacheInvalidator;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchoolFestivalsV1CacheInvalidateScheduler {

    private final CacheInvalidator cacheInvalidator;

    // 매일 정각마다 캐시 초기화
    @Scheduled(cron = "0 0 0 * * *")
    public void invalidate() {
        cacheInvalidator.invalidate(SchoolFestivalsV1QueryService.SCHOOL_FESTIVALS_V1_CACHE_NAME);
        cacheInvalidator.invalidate(SchoolFestivalsV1QueryService.PAST_SCHOOL_FESTIVALS_V1_CACHE_NAME);
    }
}
