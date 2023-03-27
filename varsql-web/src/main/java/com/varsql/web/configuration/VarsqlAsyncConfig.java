package com.varsql.web.configuration;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import com.varsql.web.constants.ResourceConfigConstants;

@Configuration
@EnableAsync
public class VarsqlAsyncConfig {

    @Bean(name = ResourceConfigConstants.APP_WEB_SOCKET_TASK_EXECUTOR)
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(30);
        taskExecutor.setQueueCapacity(30);
        taskExecutor.setThreadNamePrefix("WebSocket-Executor-");
        taskExecutor.initialize();
        
        return taskExecutor;
    }
    
    @Bean(name = ResourceConfigConstants.APP_LOG_TASK_EXECUTOR)
    public Executor logThreadPoolTaskExecutor() {
    	ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    	taskExecutor.setCorePoolSize(3);
    	taskExecutor.setMaxPoolSize(10);
    	taskExecutor.setQueueCapacity(5);
    	taskExecutor.setThreadNamePrefix("AppLog-Executor-");
    	taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
    	taskExecutor.setAwaitTerminationSeconds(30);
    	taskExecutor.initialize(); // thread 초기화 
    	
    	
    	return new DelegatingSecurityContextAsyncTaskExecutor(taskExecutor) {
            public void shutdown() {
            	taskExecutor.destroy();
            }
        };
        
//    	// security 적용 하기 위해서 한번 감싸줌. 
//    	return new DelegatingSecurityContextAsyncTaskExecutor(taskExecutor);
    }
}