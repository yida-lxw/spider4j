/*
 * Copyright (c) 2015, Xinlong.inc and/or its affiliates. All rights reserved.
 */
package com.yida.spider4j.crawler.config;

/**
 * @ClassName: DefaultConfigurable
 * @Description: 爬虫可配置接口默认实现
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月29日 上午9:23:21
 *
 */
public abstract class DefaultConfigurable implements Configurable {
	protected CrawlConfig config;
	
	public DefaultConfigurable() {
		initConfig();
	}
	
	/**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: initConfig
     * @Description: 初始化爬虫配置对象
     * @param runnable
     * @return void
     * @throws
     */
	@Override
	public CrawlConfig initConfig() {
		this.config = CrawlConfig.getInstance();
		return this.config;
	}

	public CrawlConfig getConfig() {
		return config;
	}

	public void setConfig(CrawlConfig config) {
		this.config = config;
	}
}
