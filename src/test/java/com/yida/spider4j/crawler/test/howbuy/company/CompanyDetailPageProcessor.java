package com.yida.spider4j.crawler.test.howbuy.company;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.processor.SimpleDetailPageProcessor;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.selector.ExpressionType;
import com.yida.spider4j.crawler.utils.common.StringUtils;

/**
 * @ClassName: CompanyDetailPageProcessor
 * @Description: 好买网基金公司详情页处理器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午2:40:10
 *
 */
public class CompanyDetailPageProcessor extends SimpleDetailPageProcessor {
	public CompanyDetailPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
	}
	
	@Override
	public void process(Page page) {
		super.process(page);
		Document doc = page.getHtml(ExpressionType.JSOUP).getDocument();
		
		//基金公司唯一编号
		String pageUrl = page.getRequest().getUrl();
		String companyCode = "";
		if(StringUtils.isNotEmpty(pageUrl)) {
			companyCode = pageUrl.replace(".html","").replaceAll("/$", "");
			companyCode = companyCode.substring(companyCode.lastIndexOf("/") + 1);
		}
		page.putField("companyCode", companyCode);
		
		//基金公司名字
		String companyName = page.getHtml(ExpressionType.JSOUP)
				.jsoup("div[class=content1 clearfix] > div[class=con_left fl] > h2",
						"text").get();
		if(StringUtils.isNotEmpty(companyName)) {
			companyName = companyName.trim().replace("--", "");
		}
		page.putField("companyName", companyName);
		
		//基金公司全称
		String companyAllName = page.getHtml(ExpressionType.JSOUP)
				.jsoup("div[class=people] td[class=tdlt]",
						"text").get();
		if(StringUtils.isNotEmpty(companyAllName)) {
			companyAllName = companyAllName.trim().replace("--", "");
		}
		page.putField("companyAllName", companyAllName);
		
		//备案号
		String recordNO = page.getHtml(ExpressionType.JSOUP)
				.jsoup("li:containsOwn(备案证号) > span",
						"text").get();
		if(StringUtils.isNotEmpty(recordNO)) {
			recordNO = recordNO.trim().replace("--", "");
		}
		page.putField("recordNO", recordNO);
		
		//所在区域
		String area = page.getHtml(ExpressionType.JSOUP)
				.jsoup("li:containsOwn(所在区域) > span",
						"text").get();
		if(StringUtils.isNotEmpty(area)) {
			area = area.trim().replace("--", "");
		}
		page.putField("area", area);
		
		//成立日期
		String setupDate = page.getHtml(ExpressionType.JSOUP)
				.jsoup("li:containsOwn(成立日期) > span",
						"text").get();
		if(StringUtils.isNotEmpty(setupDate)) {
			setupDate = setupDate.trim().replace("--", "");
		}
		page.putField("setupDate", setupDate);
		
		//旗下经理
		String manager = "";
		try {
			Elements els = doc.select("li:containsOwn(旗下经理) > a");
			for(Element a : els) {
				manager += a.text() + ",";
			}
		} catch (Exception e) {
		}
		manager = manager.replace("--","");
		manager = manager.replaceAll(",$", "");
		page.putField("manager", manager);
		
		//核心人物
		String coreMember = "";
		try {
			Elements els = doc.select("div[class=people_list clearfix]");
			for(Element div : els) {
				String name = div.select("div[class=people_left] > a").first().text().trim();
				String zhiwei = div.select("div[class=people_right] > p[class=peo_title]").first().text().trim().replace(" ", "");
				zhiwei = zhiwei.replace("职位：", "").replace("--","");
				coreMember += (name + "-" + zhiwei) + ",";
			}
		} catch (Exception e) {
		}
		coreMember = coreMember.replace("--","");
		coreMember = coreMember.replaceAll(",$", "");
		page.putField("coreMember", coreMember);
		
		//团队成员
		String team = "";
		try {
			Elements els = doc.select("div[class=people] > div[class=team_list] > p[class=team_name]");
			for(Element p : els) {
				String name = p.text().trim();
				String zhiwei = p.parent().select("p[class=team_title]").first().text().trim().replace(" ", "");
				zhiwei = zhiwei.replace("职位：", "").replace("--","").replace(" ", "");
				team += (name + "-" + zhiwei) + ",";
			}
		} catch (Exception e) {
		}
		team = team.replace("--","");
		team = team.replaceAll(",$", "");
		page.putField("team", coreMember);
		
		//旗下基金
		page.putField("fundCount", page.getHtml(ExpressionType.JSOUP)
			.jsoup("li:containsOwn(旗下基金) > a",
				"text").get().replace("--", "").replace("只", ""));
		
		//注册资本
		page.putField("registZB", page.getHtml(ExpressionType.JSOUP)
			.jsoup("li:containsOwn(注册资本) > span",
				"text").get().replace("--", "").replace("（人民币）", ""));	
		
		
	}
}
