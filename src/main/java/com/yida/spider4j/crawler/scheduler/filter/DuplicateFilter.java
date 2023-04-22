package com.yida.spider4j.crawler.scheduler.filter;

import com.yida.spider4j.crawler.core.Request;
import com.yida.spider4j.crawler.task.Task;

/**
 * @ClassName: DuplicateFilter
 * @Description: URL重复过滤器接口
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月12日 上午9:10:50
 *
 */
public interface DuplicateFilter {
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: isDuplicate
	 * @Description: 检查URL调度器中URL是否有重复
	 * @param @param request
	 * @param @param task
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean isDuplicate(Request request, Task task);
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: resetDuplicateCheck
	 * @Description: 重置重复检测
	 * @param @param task
	 * @return void
	 * @throws
	 */
	public void resetDuplicateCheck(Task task);
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getTotalRequestsCount
	 * @Description: 获取请求URL数量
	 * @param @param task
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int getTotalRequestsCount(Task task);
}
