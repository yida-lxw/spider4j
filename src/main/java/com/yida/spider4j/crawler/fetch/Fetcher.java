package com.yida.spider4j.crawler.fetch;
/**
 * 网页内容提取接口
 * @author  Lanxiaowei
 * @created 2013-09-11 20:42:50
 */
public interface Fetcher {
	/**
	 * 网页提取
	 * @param page
	 * @return  返回网页提取自定义状态码,具体请参阅FetchStatus类
	 * @throws PageFetchFailureException
	 */
	public int fetch(Page page) throws PageFetchFailureException;
	
	
	public boolean isExists() throws PageFetchFailureException;
}
