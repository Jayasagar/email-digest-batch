package com.jay.emaildigest.batch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class TaskExecutorConfig {

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        //executor.setMaxPoolSize(10);
        //executor.setQueueCapacity(20);
        executor.setThreadNamePrefix("Komoot-hourly-digest-");
        executor.initialize();
        return executor;
    }
}