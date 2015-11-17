package com.yida.spider4j.crawler.test.adidas;

import com.yida.spider4j.crawler.core.PageType;
import com.yida.spider4j.crawler.core.Spider;
import com.yida.spider4j.crawler.pipeline.ConsolePipeline;
import com.yida.spider4j.crawler.processor.param.DetailPageProcessorParam;
import com.yida.spider4j.crawler.processor.param.MultiPageProcessorParam;
import com.yida.spider4j.crawler.test.adidas.processor.AdidasDetailPageProcessor;
import com.yida.spider4j.crawler.test.adidas.processor.AdidasListPageProcessor;
import com.yida.spider4j.crawler.test.adidas.processor.AdidasSeedPageProcessor;
import com.yida.spider4j.crawler.test.adidas.processor.AdidasStartPageProcessor;
import com.yida.spider4j.crawler.utils.HttpConstant;

/**
 * @ClassName: AdidasSpider
 * @Description: 阿迪达斯官网爬取测试
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月24日 下午7:51:18
 *
 */
public class AdidasSpider {
	public static void main(String[] args) {
		//seed
		MultiPageProcessorParam seedPageProcessorParam = new MultiPageProcessorParam();
		seedPageProcessorParam.setTargetUrlJsoup(
			"div[class=hfNavDetail] > a");
		//获取href属性值
		seedPageProcessorParam.setAttributeName("href");
		AdidasSeedPageProcessor seedPageProcessor = new AdidasSeedPageProcessor(seedPageProcessorParam);
				
		//start
		MultiPageProcessorParam startPageProcessorParam = new MultiPageProcessorParam();
		AdidasStartPageProcessor startPageProcessor = new AdidasStartPageProcessor(startPageProcessorParam);
				
		//list
		MultiPageProcessorParam listPageProcessorParam = new MultiPageProcessorParam();
		listPageProcessorParam.setTargetUrlJsoup(
			"div[class=productTileWrapper] > a[class=image]");
		//获取href属性值
		listPageProcessorParam.setAttributeName("href");
		AdidasListPageProcessor listPageProcessor = new AdidasListPageProcessor(listPageProcessorParam);
		
		
		//detail
		DetailPageProcessorParam detailPageProcessorParam = new DetailPageProcessorParam();
		AdidasDetailPageProcessor detailPageProcessor = new AdidasDetailPageProcessor(detailPageProcessorParam);

		
		String startUrl = "http://shop.adidas.cn/";
		Spider.create(seedPageProcessor,startPageProcessor,listPageProcessor, detailPageProcessor)
			.addHeader("host", "shop.adidas.cn")
			.addHeader("Upgrade-Insecure-Requests", "1")
			//.addHeader("Accept-Encoding", "GZIP") 
			.startUrl(startUrl,PageType.SEED_PAGE,HttpConstant.Method.GET)
			.addPipeline(new ConsolePipeline())
			.threadNum(20)
			.start();
	}
}
