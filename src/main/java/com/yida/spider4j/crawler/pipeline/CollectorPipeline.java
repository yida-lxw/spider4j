package com.yida.spider4j.crawler.pipeline;

import java.util.List;
/**
 * @ClassName: CollectorPipeline
 * @Description: 用于收集数据的管道
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月15日 上午9:05:40
 *
 */
public interface CollectorPipeline<T> extends Pipeline {
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getCollected
	 * @Description: 返回收集到的所有数据
	 * @param @return
	 * @return List<T>
	 * @throws
	 */
	public List<T> getCollected();
}
