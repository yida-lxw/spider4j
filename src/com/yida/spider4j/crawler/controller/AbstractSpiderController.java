package com.yida.spider4j.crawler.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.yida.spider4j.crawler.core.Spider;

public abstract class AbstractSpiderController implements SpiderController {
	/**爬虫实例*/
	private List<Spider> spiders;
	/**线程池管理器*/
	private ExecutorService executorService;
	
	private final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1000);
	
	public AbstractSpiderController() {
		this.executorService = new ThreadPoolExecutor(10, 
	    		800,
	            2000, 
	            TimeUnit.MILLISECONDS,queue);
	}
	
	@Override
	public void startUp() {
		if(null == spiders || spiders.size() <= 0) {
			return;
		}
		for(Spider spider : spiders) {
			//invokeTime <= 0表示需要一直循环执行下去,永不停止
			executorService.execute(spider);
		}
		//释放线程池资源
		executorService.shutdown();
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: addSpider
	 * @Description: 添加一个爬虫实例
	 * @param @param spider
	 * @param @return
	 * @return AbstractSpiderController
	 * @throws
	 */
	protected AbstractSpiderController addSpider(Spider spider) {
		if(null == spiders || spiders.size() <= 0) {
			spiders = new ArrayList<Spider>();
		}
		spiders.add(spider);
		return this;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: addSpiders
	 * @Description: 批量往爬虫控制器中添加爬虫实例
	 * @param @param spiderArray
	 * @param @return
	 * @return AbstractSpiderController
	 * @throws
	 */
	protected AbstractSpiderController addSpiders(Spider[] spiderArray) {
		if(null == spiders || spiders.size() <= 0) {
			spiders = new ArrayList<Spider>();
		}
		spiders.addAll(Arrays.asList(spiderArray));
		return this;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: addSpiders
	 * @Description: 批量往爬虫控制器中添加爬虫实例(重载)
	 * @param @param spiderList
	 * @param @return
	 * @return AbstractSpiderController
	 * @throws
	 */
	protected AbstractSpiderController addSpiders(List<Spider> spiderList) {
		if(null == spiders || spiders.size() <= 0) {
			spiders = new ArrayList<Spider>();
		}
		spiders.addAll(spiderList);
		return this;
	}

	public List<Spider> getSpiders() {
		return spiders;
	}

	public void setSpiders(List<Spider> spiders) {
		this.spiders = spiders;
	}
}
