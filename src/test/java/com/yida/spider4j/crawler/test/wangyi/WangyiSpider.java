package com.yida.spider4j.crawler.test.wangyi;

/**
 * @ClassName: DoubanSpider
 * @Description: 豆瓣电影爬虫测试类
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午7:14:02
 *
 */
public class WangyiSpider {
	public static void main(String[] args) {
		//start:指定起始页.然后解析出分页请求URL
		//list:解析出详情页URL
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
