package com.yida.spider4j.crawler.test.douban.pageprocessor;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.processor.SimpleDetailPageProcessor;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.selector.ExpressionType;

/**
 * @ClassName: DoubanDetailPageProcessor
 * @Description: 豆瓣电影Top250详情页处理器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午2:40:10
 *
 */
public class DoubanDetailPageProcessor extends SimpleDetailPageProcessor {
	public DoubanDetailPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
	}
	
	@Override
	public void process(Page page) {
		super.process(page);
		String movieName = page.getHtml(ExpressionType.JSOUP).jsoup("span[property=v:itemreviewed]","text").get();
		page.putField("movieName", movieName);
		page.putField("logo", 
			page.getHtml(ExpressionType.XPATH).xpath("//div[@id='mainpic']/a[@class='nbgnbg']/img/attribute::src").get());
		//xpath:3个参数依次是xpath表达式,提取的属性名称,是否提取多个[可能XPath表达式会匹配到多个节点]
		//注意,返回多个值,最后需要调用all(),get()只返回第一个
		page.putField("movieType", page.getHtml(ExpressionType.XPATH).xpath(
			"//span[contains(text(),'类型')]/following-sibling::span[@property='v:genre'] | "
	    	+ "//span[contains(text(),'制片国家/地区:')]/preceding-sibling::span[@property='v:genre']","text",true).all());
		page.putField("language", page.getHtml(ExpressionType.XPATH).xpath("//span[contains(text(),'语言')]/following-sibling::text()").get());
		
		//FileUtils.writeFile(movieName + "\n", "C:/movie.txt", "UTF-8", true);
	}
}
