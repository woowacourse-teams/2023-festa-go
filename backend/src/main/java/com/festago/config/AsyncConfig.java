package com.festago.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

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
        executor.setCorePoolSize(8);
        executor.setQueueCapacity(10);
        executor.initialize();
        return executor;
    }
}
