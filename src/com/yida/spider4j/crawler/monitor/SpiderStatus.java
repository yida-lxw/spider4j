package com.yida.spider4j.crawler.monitor;

import java.util.Date;
import java.util.List;

import com.yida.spider4j.crawler.core.Spider;
import com.yida.spider4j.crawler.scheduler.MonitorableScheduler;
import com.yida.spider4j.crawler.utils.log.LogUtils;

/**
 * @ClassName: SpiderStatus
 * @Description: 爬虫状态
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月15日 下午6:02:54
 *
 */
public class SpiderStatus implements SpiderStatusMXBean {

    protected final Spider spider;

    protected final SpiderMonitor.MonitorSpiderListener monitorSpiderListener;

    public SpiderStatus(Spider spider, SpiderMonitor.MonitorSpiderListener monitorSpiderListener) {
        this.spider = spider;
        this.monitorSpiderListener = monitorSpiderListener;
    }

    public String getName() {
        return spider.taskId();
    }

    public int getLeftPageCount() {
        if (spider.getScheduler() instanceof MonitorableScheduler) {
            return ((MonitorableScheduler) spider.getScheduler()).getLeftRequestsCount(spider);
        }
        LogUtils.warn("Get leftPageCount fail, try to use a Scheduler implement MonitorableScheduler for monitor count!");
        return -1;
    }

    public int getTotalPageCount() {
        if (spider.getScheduler() instanceof MonitorableScheduler) {
            return ((MonitorableScheduler) spider.getScheduler()).getTotalRequestsCount(spider);
        }
        LogUtils.warn("Get totalPageCount fail, try to use a Scheduler implement MonitorableScheduler for monitor count!");
        return -1;
    }

    @Override
    public int getSuccessPageCount() {
        return monitorSpiderListener.getSuccessCount().get();
    }

    @Override
    public int getErrorPageCount() {
        return monitorSpiderListener.getErrorCount().get();
    }

    public List<String> getErrorPages() {
        return monitorSpiderListener.getErrorUrls();
    }

    @Override
    public String getStatus() {
        return spider.getStatus().name();
    }

    @Override
    public int getThread() {
        return spider.getThreadAlive();
    }

    public void start() {
        spider.start();
    }

    public void stop() {
        spider.stop();
    }

    @Override
    public Date getStartTime() {
        return spider.getStartTime();
    }

    @Override
    public int getPagePerSecond() {
        int runSeconds = (int) (System.currentTimeMillis() - getStartTime().getTime()) / 1000;
        return getSuccessPageCount() / runSeconds;
    }

}
