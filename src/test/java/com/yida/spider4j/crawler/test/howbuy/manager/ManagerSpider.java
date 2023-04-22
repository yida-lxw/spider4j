package com.yida.spider4j.crawler.test.howbuy.manager;

import com.yida.spider4j.crawler.core.Spider;
import com.yida.spider4j.crawler.pipeline.ConsolePipeline;
import com.yida.spider4j.crawler.processor.param.DetailPageProcessorParam;
import com.yida.spider4j.crawler.processor.param.MultiPageProcessorParam;
import com.yida.spider4j.crawler.utils.HttpConstant;

/**
 * @ClassName: CompanySpider
 * @Description: 好买网基金经理
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月21日 下午1:57:23
 *
 */
public class ManagerSpider {
	public static void main(String[] args) {
		//start
		MultiPageProcessorParam startPageProcessorParam = new MultiPageProcessorParam();
		ManagerStartPageProcessor startPageProcessor = new ManagerStartPageProcessor(startPageProcessorParam);
				
		//list
		MultiPageProcessorParam listPageProcessorParam = new MultiPageProcessorParam();
		listPageProcessorParam.setTargetUrlJsoup(
			"div[class=fund_list] > table > tbody > tr[class=tr_normal] > td:eq(1) > a");
		//获取href属性值
		listPageProcessorParam.setAttributeName("href");
		ManagerListPageProcessor listPageProcessor = new ManagerListPageProcessor(listPageProcessorParam);
		
		
		//detail
		DetailPageProcessorParam detailPageProcessorParam = new DetailPageProcessorParam();
		ManagerDetailPageProcessor detailPageProcessor = new ManagerDetailPageProcessor(detailPageProcessorParam);
		
		String startUrl = "http://simu.howbuy.com/manager/";
		Spider.create(startPageProcessor,listPageProcessor, detailPageProcessor)
			.startUrl(startUrl,HttpConstant.Method.GET)
			.addPipeline(new ConsolePipeline())
			.threadNum(20)
			.start();
	}
	
	public Spider init() {
		//start
		MultiPageProcessorParam startPageProcessorParam = new MultiPageProcessorParam();
		ManagerStartPageProcessor startPageProcessor = new ManagerStartPageProcessor(startPageProcessorParam);
				
		//list
		MultiPageProcessorParam listPageProcessorParam = new MultiPageProcessorParam();
		listPageProcessorParam.setTargetUrlJsoup(
			"div[class=fund_list] > table > tbody > tr[class=tr_normal] > td:eq(1) > a");
		//获取href属性值
		listPageProcessorParam.setAttributeName("href");
		ManagerListPageProcessor listPageProcessor = new ManagerListPageProcessor(listPageProcessorParam);
		
		
		//detail
		DetailPageProcessorParam detailPageProcessorParam = new DetailPageProcessorParam();
		ManagerDetailPageProcessor detailPageProcessor = new ManagerDetailPageProcessor(detailPageProcessorParam);
		
		String startUrl = "http://simu.howbuy.com/manager/";
		return Spider.create(startPageProcessor,listPageProcessor, detailPageProcessor)
			.startUrl(startUrl,HttpConstant.Method.GET)
			.addPipeline(new ConsolePipeline())
			.threadNum(20);
	}
}
