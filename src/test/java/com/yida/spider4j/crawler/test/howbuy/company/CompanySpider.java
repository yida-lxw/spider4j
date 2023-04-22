package com.yida.spider4j.crawler.test.howbuy.company;

import com.yida.spider4j.crawler.core.Spider;
import com.yida.spider4j.crawler.pipeline.ConsolePipeline;
import com.yida.spider4j.crawler.pipeline.DataBasePipeline;
import com.yida.spider4j.crawler.processor.param.DetailPageProcessorParam;
import com.yida.spider4j.crawler.processor.param.MultiPageProcessorParam;
import com.yida.spider4j.crawler.utils.HttpConstant;

/**
 * @ClassName: CompanySpider
 * @Description: 好买网基金公司
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月21日 下午1:57:23
 *
 */
public class CompanySpider {
	public static void main(String[] args) {
		//start
		MultiPageProcessorParam startPageProcessorParam = new MultiPageProcessorParam();
		CompanyStartPageProcessor startPageProcessor = new CompanyStartPageProcessor(startPageProcessorParam);
				
		//list
		MultiPageProcessorParam listPageProcessorParam = new MultiPageProcessorParam();
		listPageProcessorParam.setTargetUrlJsoup(
			"div[class=fund_list] > table > tbody > tr[class=tr_normal] > td:eq(1) > a");
		//获取href属性值
		listPageProcessorParam.setAttributeName("href");
		CompanyListPageProcessor listPageProcessor = new CompanyListPageProcessor(listPageProcessorParam);
		
		
		//detail
		DetailPageProcessorParam detailPageProcessorParam = new DetailPageProcessorParam();
		CompanyDetailPageProcessor detailPageProcessor = new CompanyDetailPageProcessor(detailPageProcessorParam);
		
		//插入表名称
		String tableName = "pemfirms";
		DataBasePipeline databasePipeline = new DataBasePipeline(tableName);
		
		String startUrl = "http://simu.howbuy.com/company/";
		Spider.create(startPageProcessor,listPageProcessor, detailPageProcessor)
			.startUrl(startUrl,HttpConstant.Method.GET)
			.addPipeline(new ConsolePipeline())
			//.addPipeline(databasePipeline)
			.threadNum(20)
			.start();
	}
	
	public Spider init() {
		//start
		MultiPageProcessorParam startPageProcessorParam = new MultiPageProcessorParam();
		CompanyStartPageProcessor startPageProcessor = new CompanyStartPageProcessor(startPageProcessorParam);
				
		//list
		MultiPageProcessorParam listPageProcessorParam = new MultiPageProcessorParam();
		listPageProcessorParam.setTargetUrlJsoup(
			"div[class=fund_list] > table > tbody > tr[class=tr_normal] > td:eq(1) > a");
		//获取href属性值
		listPageProcessorParam.setAttributeName("href");
		CompanyListPageProcessor listPageProcessor = new CompanyListPageProcessor(listPageProcessorParam);
		
		
		//detail
		DetailPageProcessorParam detailPageProcessorParam = new DetailPageProcessorParam();
		CompanyDetailPageProcessor detailPageProcessor = new CompanyDetailPageProcessor(detailPageProcessorParam);
		
		//插入表名称
		String tableName = "pemfirms";
		//DataBasePipeline databasePipeline = new DataBasePipeline(tableName);
		
		String startUrl = "http://simu.howbuy.com/company/";
		return Spider.create(startPageProcessor,listPageProcessor, detailPageProcessor)
			.startUrl(startUrl,HttpConstant.Method.GET)
			.addPipeline(new ConsolePipeline())
			//.addPipeline(databasePipeline)
			.threadNum(20);
		
	}
}
