package com.festago.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    private static final String DEFAULT_EXECUTOR_NAME = "taskExecutor";
    public static final String FCM_EXECUTOR_NAME = "fcmExecutor";

    @Bean(name = DEFAULT_EXECUTOR_NAME)
    public Executor defaultExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.initialize();
        return executor;
    }

    @Bean(name = FCM_EXECUTOR_NAME)
    public Executor fcmExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setQueueCapacity(10);
        executor.initialize();
        return executor;
    }
}
