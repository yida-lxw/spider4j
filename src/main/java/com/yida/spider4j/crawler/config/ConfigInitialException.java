package com.yida.spider4j.crawler.config;

/**
 * 爬虫配置文件初始化异常
 * @author  Lanxiaowei
 * @created 2013-09-08 12:10:52
 */
@SuppressWarnings("serial")
public class ConfigInitialException extends RuntimeException {
	public ConfigInitialException() {
        super();
    }
	
	public ConfigInitialException(String message) {
        super(message);
    }
	
	public ConfigInitialException(String message, Throwable cause) {
        super(message, cause);
    }
}
