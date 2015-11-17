package com.yida.spider4j.crawler.test.douban;

import com.yida.spider4j.crawler.core.Spider;
import com.yida.spider4j.crawler.pipeline.ConsolePipeline;
import com.yida.spider4j.crawler.processor.param.DetailPageProcessorParam;
import com.yida.spider4j.crawler.processor.param.MultiPageProcessorParam;
import com.yida.spider4j.crawler.test.douban.pageprocessor.DoubanDetailPageProcessor;
import com.yida.spider4j.crawler.test.douban.pageprocessor.DoubanListPageProcessor;
import com.yida.spider4j.crawler.test.douban.pageprocessor.DoubanStartPageProcessor;
/**
 * @ClassName: DoubanSpider
 * @Description: 豆瓣电影爬虫测试类
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午7:14:02
 *
 */
public class DoubanSpider {
	public static void main(String[] args) {
		//start
		MultiPageProcessorParam startPageProcessorParam = new MultiPageProcessorParam();
		DoubanStartPageProcessor startPageProcessor = new DoubanStartPageProcessor(startPageProcessorParam);
				
		//list
		MultiPageProcessorParam listPageProcessorParam = new MultiPageProcessorParam();
		listPageProcessorParam.setTargetUrlJsoup(
			"div[class=item] > div[class=info] > div[class=hd] > a");
		//获取href属性值
		listPageProcessorParam.setAttributeName("href");
		
		DoubanListPageProcessor listPageProcessor = new DoubanListPageProcessor(listPageProcessorParam);
		
		//detail
		DetailPageProcessorParam detailPageProcessorParam = new DetailPageProcessorParam();
		DoubanDetailPageProcessor detailPageProcessor = new DoubanDetailPageProcessor(detailPageProcessorParam);
		
		String startUrl = "http://movie.douban.com/top250";
		Spider.create(startPageProcessor,listPageProcessor, detailPageProcessor).startUrl(startUrl)
			.addPipeline(new ConsolePipeline())
			.threadNum(20)
			.start();
		
	}
	
	public Spider init() {
		//start
		MultiPageProcessorParam startPageProcessorParam = new MultiPageProcessorParam();
		DoubanStartPageProcessor startPageProcessor = new DoubanStartPageProcessor(startPageProcessorParam);
				
		//list
		MultiPageProcessorParam listPageProcessorParam = new MultiPageProcessorParam();
		listPageProcessorParam.setTargetUrlJsoup(
			"div[class=item] > div[class=info] > div[class=hd] > a");
		//获取href属性值
		listPageProcessorParam.setAttributeName("href");
		
		DoubanListPageProcessor listPageProcessor = new DoubanListPageProcessor(listPageProcessorParam);
		
		//detail
		DetailPageProcessorParam detailPageProcessorParam = new DetailPageProcessorParam();
		DoubanDetailPageProcessor detailPageProcessor = new DoubanDetailPageProcessor(detailPageProcessorParam);
		
		String startUrl = "http://movie.douban.com/top250";
		Spider spider = Spider.create(startPageProcessor,listPageProcessor, detailPageProcessor).startUrl(startUrl)
			.addPipeline(new ConsolePipeline())
			.threadNum(20);
		//注意这里并没有去start()启动爬虫,这里仅仅是初始化Spider对象,
		//然后把爬虫对象交给SipderController爬虫控制器去管理
		return spider;
	}
}
