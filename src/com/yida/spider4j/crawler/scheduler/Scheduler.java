package com.yida.spider4j.crawler.scheduler;

import com.yida.spider4j.crawler.core.Request;
import com.yida.spider4j.crawler.task.Task;

/**
 * @ClassName: Scheduler
 * @Description: URL调度接口
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月12日 上午9:07:23
 *
 */
public interface Scheduler {
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: push
	 * @Description: 把一个URL放入调度器
	 * @param @param request
	 * @param @param task
	 * @return void
	 * @throws
	 */
	public void push(Request request, Task task);
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: poll
	 * @Description: 从调度器poll出一个URL用来爬取
	 * @param @param task
	 * @param @return
	 * @return Request
	 * @throws
	 */
	public Request poll(Task task);
}
