package com.yida.spider4j.crawler.scheduler;

import com.yida.spider4j.crawler.task.Task;
/**
 * @ClassName: MonitorableScheduler
 * @Description: URL调度器-监听器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月12日 下午6:32:11
 *
 */
public interface MonitorableScheduler extends Scheduler {
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getLeftRequestsCount
	 * @Description: 剩余尚未爬取的URL数量
	 * @param @param task
	 * @param @return
	 * @return int
	 * @throws
	 */
    public int getLeftRequestsCount(Task task);

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getTotalRequestsCount
     * @Description: URL调度器里URL总数量
     * @param @param task
     * @param @return
     * @return int
     * @throws
     */
    public int getTotalRequestsCount(Task task);

}