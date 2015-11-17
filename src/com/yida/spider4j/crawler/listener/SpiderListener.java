package com.yida.spider4j.crawler.listener;

import com.yida.spider4j.crawler.core.Request;

/**
 * @ClassName: SpiderListener
 * @Description: 爬虫监听器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月15日 下午5:27:41
 *
 */
public interface SpiderListener {
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: onSuccess
	 * @Description: 网页下载成功时的回调函数
	 * @param @param request
	 * @return void
	 * @throws
	 */
	public void onSuccess(Request request);

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: onError
	 * @Description: 网页下载失败时的回调函数
	 * @param @param request
	 * @return void
	 * @throws
	 */
    public void onError(Request request);
}
