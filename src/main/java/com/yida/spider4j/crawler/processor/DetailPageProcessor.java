package com.yida.spider4j.crawler.processor;

import com.yida.spider4j.crawler.core.Page;

/**
 * @ClassName: DetailPageProcessor
 * @Description: 详情页PageProcessor实现
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月13日 下午3:42:47
 *
 */
public interface DetailPageProcessor extends PageProcessor {
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: process
	 * @Description: 网页处理逻辑(解析网页数据)
	 * @param @param page
	 * @return void
	 * @throws
	 */
	public void process(Page page);
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: checkValid
	 * @Description: 检测网页是否有效[有时,可能网页是下载成功了,可是网页里并没有我们需要提取的数据,
	 *               这时,我们视之为无效的网页]
	 * @param @param page
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean checkValid(Page page);

	/**
	 * 发送额外的请求用于补充详情页获取不到的数据，
	 * 比如详情页的价格或评论等数据可能需要额外一次Ajax请求来加载
	 * @param page
	 */
	void buildExtraRequest(Page page);

	/**生成详情页的附属页的请求URL*/
	String generateAttachedPageURL(Page page);
}
