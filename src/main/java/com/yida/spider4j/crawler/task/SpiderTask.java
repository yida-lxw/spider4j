package com.yida.spider4j.crawler.task;

import com.yida.spider4j.crawler.wangyi.WangyiSpider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SpiderTask {
    private static final Logger log = LoggerFactory.getLogger(SpiderTask.class);

    @Async("spiderThreadPool")
    @Scheduled(cron="0 0/5 * * * ?")
    public void spiderScheduleTask() {
        log.info("开始抓取网易新闻[每间隔5分钟自动抓取]");
        WangyiSpider.run();
    }
}
