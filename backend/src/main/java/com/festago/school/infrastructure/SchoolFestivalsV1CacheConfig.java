package com.festago.school.infrastructure;

import com.festago.school.application.v1.SchoolFestivalsV1QueryService;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchoolFestivalsV1CacheConfig {

    private static final long EXPIRED_AFTER_WRITE = 30;
    private static final long MAXIMUM_SIZE = 1_000;

    @Bean
    public Cache schoolFestivalsV1Cache() {
        return new CaffeineCache(SchoolFestivalsV1QueryService.SCHOOL_FESTIVALS_V1_CACHE_NAME,
            Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(EXPIRED_AFTER_WRITE, TimeUnit.MINUTES)
                .maximumSize(MAXIMUM_SIZE)
                .build()
        );
    }

    @Bean
    public Cache pastSchoolFestivalsV1Cache() {
        return new CaffeineCache(SchoolFestivalsV1QueryService.PAST_SCHOOL_FESTIVALS_V1_CACHE_NAME,
            Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(EXPIRED_AFTER_WRITE, TimeUnit.MINUTES)
                .maximumSize(MAXIMUM_SIZE)
                .build()
        );
    }
}
