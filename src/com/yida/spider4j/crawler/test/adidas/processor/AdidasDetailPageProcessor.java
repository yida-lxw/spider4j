package com.yida.spider4j.crawler.test.adidas.processor;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.processor.SimpleDetailPageProcessor;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.selector.ExpressionType;
import com.yida.spider4j.crawler.selector.Html;

/**
 * @ClassName: AdidasDetailPageProcessor
 * @Description: 阿迪达斯详情页处理器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午2:40:10
 *
 */
public class AdidasDetailPageProcessor extends SimpleDetailPageProcessor {
	public AdidasDetailPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
	}
	
	@Override
	public void process(Page page) {
		super.process(page);
		Html jsoup = page.getHtml(ExpressionType.JSOUP);
		Html xpath = page.getHtml(ExpressionType.XPATH);
		

		//商品唯一编号(货号)
		page.putField("goodsNO", jsoup.jsoup("h1[class=pdpTit] > em","text").get());
		
		//标题
		page.putField("title", jsoup.jsoup("h1[class=pdpTit]","text").get());
		
		//商品图片
		page.putField("goodsImg", jsoup.jsoup("div[class=skuImg] > img[class=skuZoom]","src").get());
		
		//商品现价
		page.putField("presentPrice", xpath.xpath("//p[@class='pdpPrice']/span[@class='price']","text").get());
		
		//商品原价
		page.putField("originalPrice", xpath.xpath("//p[@class='pdpPrice']/del/span[@class='price']","text").get());
		
		//颜色
		page.putField("color", xpath.xpath("//strong[contains(text(),'颜 色：')]/following-sibling::text()").get());
		
		//品牌系列
		page.putField("brandType", xpath.xpath("//strong[contains(text(),'品牌系列：')]/following-sibling::text()").get());
		
		//产品类目
		page.putField("category", xpath.xpath("//strong[contains(text(),'产品类目：')]/following-sibling::text()").get());
		
		//产品类型
		page.putField("goodsType", xpath.xpath("//strong[contains(text(),'产品类型：')]/following-sibling::text()").get());
		
		//面料
		page.putField("fabric", xpath.xpath("//strong[contains(text(),'面 料：')]/following-sibling::text()").get());
	}
}
