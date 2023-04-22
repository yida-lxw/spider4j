package com.yida.spider4j.crawler.pipeline;

import com.yida.spider4j.crawler.core.PageResultItem;
import com.yida.spider4j.crawler.task.Task;

/**
 * @ClassName: Pipeline
 * @Description: 数据消费管道[数据消费即数据存储到数据库,数据写入文件,数据打印到控制台等等]
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月14日 下午6:03:47
 *
 */
public interface Pipeline {
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: process
	 * @Description: 处理提取到的数据
	 * @param @param pageResultItem
	 * @param @param task
	 * @return void
	 * @throws
	 */
	public void process(PageResultItem pageResultItem, Task task);
}
