package com.yida.spider4j.crawler.monitor;

import java.util.Date;
import java.util.List;


public interface SpiderStatusMXBean {
	/**返回爬虫的唯一标识名*/
    public String getName();

    /**返回爬虫的当前状态*/
    public String getStatus();

    /**返回线程数量*/
    public int getThread();
    
    /**总的URL数量*/
    public int getTotalPageCount();

    /**剩余尚未爬取的URL数量*/
    public int getLeftPageCount();

    /**爬取成功的URL数量*/
    public int getSuccessPageCount();

    /**爬取失败的URL数量*/
    public int getErrorPageCount();

    /**爬取失败的URL集合*/
    public List<String> getErrorPages();

    /**爬虫启动之前要做的处理逻辑*/
    public void start();

    /**爬虫停止之前要做的处理逻辑*/
    public void stop();

    /**爬虫启动时间*/
    public Date getStartTime();

    /**每秒爬取的网页数量*/
    public int getPagePerSecond();
}
