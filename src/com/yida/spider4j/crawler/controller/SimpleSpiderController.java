package com.yida.spider4j.crawler.controller;

import com.yida.spider4j.crawler.core.Spider;
import com.yida.spider4j.crawler.test.howbuy.company.CompanySpider;
import com.yida.spider4j.crawler.test.howbuy.fund.FundSpider;
import com.yida.spider4j.crawler.test.howbuy.manager.ManagerSpider;

/**
 * @ClassName: SimpleSpiderController
 * @Description: SpiderController爬虫控制器的默认简单实现
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月27日 下午3:59:11
 *
 */
public class SimpleSpiderController extends AbstractSpiderController {
	public static void main(String[] args) {
		//初始化爬虫实例
		CompanySpider companySpider = new CompanySpider();
		Spider spider1 = companySpider.init();
		ManagerSpider managerSpider = new ManagerSpider();
		Spider spider2 = managerSpider.init();
		FundSpider fundSpider = new FundSpider();
		Spider spider3 = fundSpider.init();
		
		//创建爬虫控制器
		SimpleSpiderController spiderController = new SimpleSpiderController();
		spiderController.addSpider(spider1).addSpider(spider2).addSpider(spider3);
		
		//控制器开始工作
		spiderController.startUp();
	}
}
