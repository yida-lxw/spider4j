package com.yida.spider4j.crawler.test.howbuy.fund;

import com.yida.spider4j.crawler.core.Spider;
import com.yida.spider4j.crawler.pipeline.ConsolePipeline;
import com.yida.spider4j.crawler.processor.param.DetailPageProcessorParam;
import com.yida.spider4j.crawler.processor.param.MultiPageProcessorParam;
import com.yida.spider4j.crawler.utils.HttpConstant;

/**
 * @ClassName: FundSpider
 * @Description: 好买网基金产品
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月21日 下午1:57:23
 *
 */
public class FundSpider {
	public static void main(String[] args) {
		//start
		MultiPageProcessorParam startPageProcessorParam = new MultiPageProcessorParam();
		FundStartPageProcessor startPageProcessor = new FundStartPageProcessor(startPageProcessorParam);
				
		//list
		MultiPageProcessorParam listPageProcessorParam = new MultiPageProcessorParam();
		listPageProcessorParam.setTargetUrlJsoup(
			"div[id=smpmView] > div[class=result_table result_data0] > table[id=spreadDetails] > tbody > tr[class=tr_normal] > td:eq(2) > a");
		//获取href属性值
		listPageProcessorParam.setAttributeName("href");
		FundListPageProcessor listPageProcessor = new FundListPageProcessor(listPageProcessorParam);
		
		
		//detail
		DetailPageProcessorParam detailPageProcessorParam = new DetailPageProcessorParam();
		FundDetailPageProcessor detailPageProcessor = new FundDetailPageProcessor(detailPageProcessorParam);
		
		String startUrl = "http://simu.howbuy.com/mlboard.htm";
		Spider.create(startPageProcessor,listPageProcessor, detailPageProcessor)
			.startUrl(startUrl,HttpConstant.Method.GET)
			.addPipeline(new ConsolePipeline())
			.threadNum(20)
			.start();
	}
	
	public Spider init() {
		//start
		MultiPageProcessorParam startPageProcessorParam = new MultiPageProcessorParam();
		FundStartPageProcessor startPageProcessor = new FundStartPageProcessor(startPageProcessorParam);
				
		//list
		MultiPageProcessorParam listPageProcessorParam = new MultiPageProcessorParam();
		listPageProcessorParam.setTargetUrlJsoup(
			"div[id=smpmView] > div[class=result_table result_data0] > table[id=spreadDetails] > tbody > tr[class=tr_normal] > td:eq(2) > a");
		//获取href属性值
		listPageProcessorParam.setAttributeName("href");
		FundListPageProcessor listPageProcessor = new FundListPageProcessor(listPageProcessorParam);
		
		
		//detail
		DetailPageProcessorParam detailPageProcessorParam = new DetailPageProcessorParam();
		FundDetailPageProcessor detailPageProcessor = new FundDetailPageProcessor(detailPageProcessorParam);
		
		String startUrl = "http://simu.howbuy.com/mlboard.htm";
		return Spider.create(startPageProcessor,listPageProcessor, detailPageProcessor)
			.startUrl(startUrl,HttpConstant.Method.GET)
			.addPipeline(new ConsolePipeline())
			.threadNum(20);
	}
}
