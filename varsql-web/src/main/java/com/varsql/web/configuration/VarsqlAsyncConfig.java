package com.varsql.web.configuration;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.varsql.web.constants.ResourceConfigConstants;

@Configuration
@EnableAsync
public class VarsqlAsyncConfig {

    @Bean(name = ResourceConfigConstants.APP_WEB_SOCKET_TASK_EXECUTOR)
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(3);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(20);
        taskExecutor.setThreadNamePrefix("WebSocket-Executor-");
        taskExecutor.initialize();
        return taskExecutor;
    }
    
    @Bean(name = ResourceConfigConstants.APP_LOG_TASK_EXECUTOR)
    public Executor logThreadPoolTaskExecutor() {
    	ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    	taskExecutor.setCorePoolSize(3);
    	taskExecutor.setMaxPoolSize(10);
    	taskExecutor.setQueueCapacity(10);
    	taskExecutor.setThreadNamePrefix("AppLog-Executor-");
    	taskExecutor.initialize();
    	return taskExecutor;
    }

}