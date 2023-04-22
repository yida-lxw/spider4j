package com.yida.spider4j.crawler.test.tencent;

import com.yida.spider4j.crawler.core.Spider;
import com.yida.spider4j.crawler.pipeline.ConsolePipeline;
import com.yida.spider4j.crawler.processor.param.DetailPageProcessorParam;
import com.yida.spider4j.crawler.processor.param.MultiPageProcessorParam;
import com.yida.spider4j.crawler.test.tencent.processor.APPDetailPageProcessor;
import com.yida.spider4j.crawler.test.tencent.processor.APPStartPageProcessor;

/**
 * @ClassName: APPSpider
 * @Description: 腾讯应用宝APP爬虫测试类
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月26日 下午5:31:55
 *
 */
public class APPSpider {
	public static void main(String[] args) {
		//start
		MultiPageProcessorParam startPageProcessorParam = new MultiPageProcessorParam();
		APPStartPageProcessor startPageProcessor = new APPStartPageProcessor(startPageProcessorParam);
				
		//list
		/*MultiPageProcessorParam listPageProcessorParam = new MultiPageProcessorParam();
		listPageProcessorParam.setTargetUrlJsoup(
			"div[class=item] > div[class=info] > div[class=hd] > a");
		//获取href属性值
		listPageProcessorParam.setAttributeName("href");
		
		APPListPageProcessor listPageProcessor = new APPListPageProcessor(listPageProcessorParam);*/
		
		//detail
		DetailPageProcessorParam detailPageProcessorParam = new DetailPageProcessorParam();
		APPDetailPageProcessor detailPageProcessor = new APPDetailPageProcessor(detailPageProcessorParam);
		
		String startUrl = "http://android.myapp.com/myapp/category.htm?orgame=1";
		Spider.create(startPageProcessor,null, detailPageProcessor).startUrl(startUrl)
			.addPipeline(new ConsolePipeline())
			.threadNum(20)
			.start();
	}
}
