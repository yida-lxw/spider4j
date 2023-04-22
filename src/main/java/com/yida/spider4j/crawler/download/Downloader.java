package com.yida.spider4j.crawler.download;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.core.Request;
import com.yida.spider4j.crawler.task.Task;

/**
 * @ClassName: Downloader
 * @Description:  网页下载器接口
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月9日 上午11:02:12
 *
 */
public interface Downloader {
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: download
	 * @Description: 下载网页
	 * @param @param request
	 * @param @param task
	 * @param @return
	 * @return Page
	 * @throws
	 */
	public Page download(Request request, Task task);
	
	/**告诉downloader,爬虫开启的线程数*/
	public void setThreadNum(int threadNum);
}
