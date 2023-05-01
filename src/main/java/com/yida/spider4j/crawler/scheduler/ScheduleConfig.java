package com.yida.spider4j.crawler.scheduler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;


/**
 * 定时任务线程池配置
 * @author yida
 * @date 2023/04/30
 */
@EnableAsync
@Configuration
public class ScheduleConfig {
    public static final int cpuCoreSize = Runtime.getRuntime().availableProcessors();

    @Bean("sparkSQLThreadPool")
    public TaskExecutor sparkSQLTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(2);
        // 设置最大线程数
        executor.setMaxPoolSize(4);
        // 设置队列容量
        executor.setQueueCapacity(1024);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(10);
        // 设置默认线程名称
        executor.setThreadNamePrefix("spark-sql-scheduled-threadpool-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    @Bean("spiderThreadPool")
    public TaskExecutor spiderTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(cpuCoreSize);
        // 设置最大线程数
        executor.setMaxPoolSize(cpuCoreSize * 2);
        // 设置队列容量
        executor.setQueueCapacity(1024);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(10);
        // 设置默认线程名称
        executor.setThreadNamePrefix("spider-scheduled-threadpool-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }
}
