package com.festago.school.infrastructure;

import com.festago.school.application.v2.SchoolFestivalsV2QueryService;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchoolFestivalsV2CacheConfig {

    private static final long EXPIRED_AFTER_WRITE = 30;
    private static final long MAXIMUM_SIZE = 1_000;

    @Bean
    public Cache schoolFestivalsV2Cache() {
        return new CaffeineCache(SchoolFestivalsV2QueryService.SCHOOL_FESTIVALS_V2_CACHE_NAME,
            Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(EXPIRED_AFTER_WRITE, TimeUnit.MINUTES)
                .maximumSize(MAXIMUM_SIZE)
                .build()
        );
    }

    @Bean
    public Cache pastschoolFestivalsV2Cache() {
        return new CaffeineCache(SchoolFestivalsV2QueryService.PAST_SCHOOL_FESTIVALS_V2_CACHE_NAME,
            Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(EXPIRED_AFTER_WRITE, TimeUnit.MINUTES)
                .maximumSize(MAXIMUM_SIZE)
                .build()
        );
    }
}
