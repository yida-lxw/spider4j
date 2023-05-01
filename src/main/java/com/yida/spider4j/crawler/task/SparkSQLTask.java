package com.yida.spider4j.crawler.task;

import com.yida.spider4j.crawler.spark.DBApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SparkSQLTask {
    private static final Logger log = LoggerFactory.getLogger(SparkSQLTask.class);

    @Async("sparkSQLThreadPool")
    @Scheduled(cron="0 0/10 * * * ?")
    public void spiderScheduleTask() {
        log.info("开始抓取网易新闻[每间隔10分钟自动抓取]");
        DBApp.start();
    }
}
