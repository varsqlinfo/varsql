package com.varsql.web.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
@EnableCaching
public class ServiceConfigurer implements AsyncConfigurer {

    private final Logger log = LoggerFactory.getLogger(ServiceConfigurer.class);

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<Cache> caches = new ArrayList<Cache>();
        // to customize
        caches.add(defaultCache());
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @Bean
    public Cache defaultCache() {
        ConcurrentMapCacheFactoryBean cacheFactoryBean = new ConcurrentMapCacheFactoryBean();
        cacheFactoryBean.setName("default");
        cacheFactoryBean.afterPropertiesSet();
        return cacheFactoryBean.getObject();

    }

    @Override
    public Executor getAsyncExecutor() {
        log.debug("Creating Async Task Executor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // to customize with your requirements
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(40);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("VarsqlExecutor-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }

}
