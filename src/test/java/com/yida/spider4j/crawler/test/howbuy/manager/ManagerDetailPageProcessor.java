package com.yida.spider4j.crawler.test.howbuy.manager;

import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.processor.SimpleDetailPageProcessor;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.selector.ExpressionType;
import com.yida.spider4j.crawler.utils.common.DateUtils;
import com.yida.spider4j.crawler.utils.common.StringUtils;

/**
 * @ClassName: ManagerDetailPageProcessor
 * @Description: 好买网基金经理详情页处理器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午2:40:10
 *
 */
public class ManagerDetailPageProcessor extends SimpleDetailPageProcessor {
	public ManagerDetailPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
	}
	
	@Override
	public void process(Page page) {
		super.process(page);
		Document doc = page.getHtml(ExpressionType.JSOUP).getDocument();
		
		String pageUrl = page.getRequest().getUrl();
		//基金经理Code[唯一的]
		String managerCode = pageUrl.substring(pageUrl.lastIndexOf("/") + 1)
			.replace(".html", "");
		page.putField("managerCode", managerCode);
		
		//基金经理名字
		String managerName = page.getHtml(ExpressionType.JSOUP)
				.jsoup("div[class=manager_name]",
						"text").get();
		if(StringUtils.isNotEmpty(managerName)) {
			managerName = managerName.replace(" ", "");
		}
		page.putField("managerName", managerName);
		
		//基金经理头像URL
		String pic = page.getHtml(ExpressionType.JSOUP)
			.jsoup("div[class=manager_pic fl] > img:eq(0)","src").get();
		if(StringUtils.isNotEmpty(pic)) {
			pic = pic.replace("--", "");
		}
		if(pic.endsWith("managerPic.png")) {
			pic = "";
		}
		page.putField("pic", pic);
		
		//管理基金数
		String fundCount = page.getHtml(ExpressionType.JSOUP)
			.jsoup("li:containsOwn(管理基金数) > span > a",
			"text").get();
		page.putField("fundCount", (StringUtils.isNotEmpty(fundCount)?
			fundCount.replace(" ", "").replace("--","").replace("只","") : null));
		
		
		//基金公司与URL
		String fundCompany = page.getHtml(ExpressionType.JSOUP)
				.jsoup("span:containsOwn(基金公司) + a",
						"text").get();
		String companyUrl = page.getHtml(ExpressionType.JSOUP)
				.jsoup("span:containsOwn(基金公司) + a",
						"href").get();
		if(StringUtils.isNotEmpty(fundCompany)) {
			fundCompany = fundCompany.replace("--","");
		}
		page.putField("fundCompany", fundCompany);
		page.putField("companyUrl", companyUrl);
		
		//擅长策略
		String strategy = page.getHtml(ExpressionType.JSOUP)
			.jsoup("td:contains(擅长策略)",
			"text").get();
		page.putField("strategy", (StringUtils.isNotEmpty(strategy)?
			strategy.replace(" ", "").replace("--","").replace("擅长策略：","") : null));
		
		//履历背景
		String background = page.getHtml(ExpressionType.JSOUP)
			.jsoup("td:contains(履历背景)","text").get();
		page.putField("background", (StringUtils.isNotEmpty(background)?
				background.replace(" ", "").replace("--","").replace("履历背景：","") : null));
		
		//任职时间
		String jobDateStr = page.getHtml(ExpressionType.JSOUP)
			.jsoup("td:contains(任职日期)","text").get();
		jobDateStr = (StringUtils.isNotEmpty(jobDateStr)?
				jobDateStr.replace(" ", "").replace("--","").replace("任职日期：","") : null);
		Date jobDate = null;
		if(StringUtils.isNotEmpty(jobDateStr)) {
			try {
				jobDate = DateUtils.stringToDate(jobDateStr, "yyyy-MM-dd");
			} catch (Exception e) {
				jobDate = null;
			}
		}
		page.putField("jobDate", jobDate);
		
		//从业年限
		String workYear = page.getHtml(ExpressionType.JSOUP)
			.jsoup("td:contains(从业年限)","text").get();
		workYear = (StringUtils.isNotEmpty(workYear)?
				workYear.replace(" ", "").replace("--","").replace("从业年限：","") : null);
		float workYearNum = 0;
		if(StringUtils.isNotEmpty(workYear)) {
			try {
				workYearNum = Float.valueOf(workYearNum);
			} catch (Exception e) {
				
			}
		}
		page.putField("workYear", workYear);
		page.putField("workYearNum", workYearNum);
		
		//学历
		String education = page.getHtml(ExpressionType.JSOUP)
			.jsoup("td:contains(学历)","text").get();
		page.putField("education", (StringUtils.isNotEmpty(education)?
				education.replace(" ", "").replace("--","").replace("学历：","") : null));
		
		//基金经理个人简历
		String managerDesc = page.getHtml(ExpressionType.JSOUP)
			.jsoup("div[class=manager_desc]","text").get();
		page.putField("managerDesc", (StringUtils.isNotEmpty(managerDesc)?
				managerDesc.replace(" ", "").replace("--","")
				.replace(managerName + ":","")
				.replace(managerName + "：",""): null));
		
		//基金公司介绍
		String companyDesc = page.getHtml(ExpressionType.JSOUP)
			.jsoup("div[class=company_desc]","text").get();
		page.putField("companyDesc", (StringUtils.isNotEmpty(companyDesc)?
				companyDesc.replace(" ", "").replace("--","") : null));
		
		//4星基金数量
		String fourStarFund = page.getHtml(ExpressionType.JSOUP)
			.jsoup("ul[class=situation] > li:eq(4)","text").get();
		if(StringUtils.isNotEmpty(fourStarFund)) {
			fourStarFund = fourStarFund.replace("只","").replace("--","");
			if("".equals(fourStarFund)) {
				fourStarFund = "0";
			}
		}
		page.putField("fourStarFund", fourStarFund);
		
		//收益率 2014年	2013年	2012年	2011年	2010年	2009年	2008年
		String shouyilv = "";
		try {
			Elements tds = doc.select("td:contains(收益率) ~ td");
			if(tds != null && tds.size() > 0) {
				for(Element td : tds) {
					String text = td.text().replace("--","").replace("%", "").trim();
					if("".equals(text)) {
						text = "0";
					}
					shouyilv += text + ",";
				}
			}
			
		} catch (NullPointerException e) {
			
		}
		if(shouyilv.length() > 0) {
			shouyilv = shouyilv.replaceAll(",$", "");
		}
		page.putField("shouyilv", shouyilv);
		
		
		//最优基金
		String bestFund = page.getHtml(ExpressionType.JSOUP)
			.jsoup("div[class=contrast_right] > p[class=fund_name] > a","text").get();
		page.putField("bestFund", (StringUtils.isNotEmpty(bestFund)?
				bestFund.replace(" ", "").replace("--","") : null));
		
		//旗下基金
		String fundlist = "";
		String fundCodeList = "";
		try {
		    Elements tds = doc.select("div[class=manager_item] > div[id=nTab9_Con1] > table > tbody > tr:gt(0) > td:eq(1)");
		    if(tds != null && tds.size() > 0) {
		        for(Element td : tds) {
		            String text = td.text().replace("--","0").trim();
		            String url = td.select("a").first().attr("href");
		            url = url.replaceAll("/$", "");
		            url = url.substring(url.lastIndexOf("/") + 1).replace(".html", "");
		            
		            fundlist += text + ",";
		            fundCodeList += url + ",";
		        }
		    }
		    
		} catch (NullPointerException e) {
			
		}
		if(fundlist.length() > 0) {
			fundlist = fundlist.replaceAll(",$", "");
		}
		if(fundCodeList.length() > 0) {
			fundCodeList = fundCodeList.replaceAll(",$", "");
		}
		page.putField("fundlist", fundlist);
		page.putField("fundCodeList", fundCodeList);
	}
}
