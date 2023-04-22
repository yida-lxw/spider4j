package com.yida.spider4j.crawler.fetch;
/**
 * 网页提取失败自定义异常
 * @author  Lanxiaowei
 * @created 2013-09-11 20:46:26
 */
@SuppressWarnings("serial")
public class PageFetchFailureException extends RuntimeException {
	public PageFetchFailureException() {
        super();
    }
	
	public PageFetchFailureException(String message) {
        super(message);
    }
	
	public PageFetchFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
