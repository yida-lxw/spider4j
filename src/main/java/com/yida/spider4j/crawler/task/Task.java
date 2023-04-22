package com.yida.spider4j.crawler.task;

import com.yida.spider4j.crawler.core.Site;

/**
 * @ClassName: Task
 * @Description: 爬虫任务接口
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月9日 上午10:58:55
 *
 */
public interface Task {
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: taskId  返回爬虫的任务ID
	 * @Description: 这里用一句话描述这个方法的作用
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String taskId();
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getSite
	 * @Description: 返回抓取任务的目标网站
	 * @param @return
	 * @return Site
	 * @throws
	 */
	public Site getSite();
}
