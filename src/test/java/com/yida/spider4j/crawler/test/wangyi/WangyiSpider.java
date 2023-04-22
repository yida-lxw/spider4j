package com.yida.spider4j.crawler.test.wangyi;

import com.yida.spider4j.crawler.core.Spider;
import com.yida.spider4j.crawler.pipeline.ConsolePipeline;
import com.yida.spider4j.crawler.processor.param.DetailPageProcessorParam;
import com.yida.spider4j.crawler.processor.param.MultiPageProcessorParam;
import com.yida.spider4j.crawler.test.wangyi.pageprocessor.WangyiDetailPageProcessor;
import com.yida.spider4j.crawler.test.wangyi.pageprocessor.WangyiListPageProcessor;
import com.yida.spider4j.crawler.test.wangyi.pageprocessor.WangyiStartPageProcessor;

/**
 * @ClassName: DoubanSpider
 * @Description: 豆瓣电影爬虫测试类
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午7:14:02
 *
 */
public class WangyiSpider {
	public static void main(String[] args) {
		String regex = "^https:\\/\\/www\\.163\\.com\\/[a-zA-Z]+\\/article\\/[a-zA-Z0-9]+\\.html\\?clickfrom=w_ent$";
		//start
		/*MultiPageProcessorParam startPageProcessorParam = new MultiPageProcessorParam();
		WangyiStartPageProcessor startPageProcessor = new WangyiStartPageProcessor(startPageProcessorParam);
				
		//list
		MultiPageProcessorParam listPageProcessorParam = new MultiPageProcessorParam();
		listPageProcessorParam.setTargetUrlJsoup(
			"div[class=item] > div[class=info] > div[class=hd] > a");
		//获取href属性值
		listPageProcessorParam.setAttributeName("href");
		
		WangyiListPageProcessor listPageProcessor = new WangyiListPageProcessor(listPageProcessorParam);
		
		//detail
		DetailPageProcessorParam detailPageProcessorParam = new DetailPageProcessorParam();
		WangyiDetailPageProcessor detailPageProcessor = new WangyiDetailPageProcessor(detailPageProcessorParam);
		
		String startUrl = "http://movie.douban.com/top250";
		Spider.create(startPageProcessor,listPageProcessor, detailPageProcessor).startUrl(startUrl)
			.addPipeline(new ConsolePipeline())
			.threadNum(20)
			.start();*/
		
	}

}
