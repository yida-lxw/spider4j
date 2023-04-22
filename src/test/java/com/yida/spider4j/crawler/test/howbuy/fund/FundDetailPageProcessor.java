package com.yida.spider4j.crawler.test.howbuy.fund;

import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.processor.SimpleDetailPageProcessor;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.selector.ExpressionType;
import com.yida.spider4j.crawler.utils.common.DateUtils;
import com.yida.spider4j.crawler.utils.common.StringUtils;

/**
 * @ClassName: FundDetailPageProcessor
 * @Description: 好买网基金产品详情页处理器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月23日 上午9:09:43
 *
 */
public class FundDetailPageProcessor extends SimpleDetailPageProcessor {
	public FundDetailPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
	}

	@Override
	public void process(Page page) {
		super.process(page);
		Document doc = page.getHtml(ExpressionType.JSOUP).getDocument();
		
		//基金产品的唯一编号
		String pageUrl = page.getRequest().getUrl();
		String fundCode = pageUrl.replace(".html", "")
			.replace("--", "").replace(" ", "");
		page.putField("fundCode", fundCode);
		
		//基金经理名字
		String fundName = page.getHtml(ExpressionType.JSOUP)
			.jsoup("div[class=trade_fund_box clearfix] > div[class=trade_fund_left fl] > div[class=trade_fund_title clearfix] > h1",
						"text").get();
		if(StringUtils.isNotEmpty(fundName)) {
			fundName = fundName.replace(" ", "");
		}
		page.putField("fundName", fundName);
		
		//基金全称
		String fundAllName = "";
		try {
			fundAllName = doc.select("td:containsOwn(基金全称) + td")
					.first().text().trim();
		} catch (NullPointerException e) {
			
		}
		fundAllName = fundAllName.replace("--", "");
		page.putField("fundAllName", fundAllName);
		
		//最新净值
		String newestJZ = "";
		//最新净值发布日期
		String newestJZDateStr = "";
		Date newestJZDate = null;
		try {
			newestJZ = doc.select("dd:containsOwn(最新净值) + dd > span")
				.first().text().trim();
			newestJZ = newestJZ.replace("*", "").replace("--", "");
			newestJZDateStr = doc.select("dd:containsOwn(最新净值) ~ dd:eq(2)")
					.first().text().trim()
					.replace("(", "").replace(")", "");
		} catch (NullPointerException e) {
			
		}
		if(StringUtils.isNotEmpty(newestJZDateStr)) {
			try {
				newestJZDate = DateUtils.stringToDate(newestJZDateStr, "yyyy-MM-dd");
			} catch (Exception e) {
				newestJZDate = null;
			}
		}
		page.putField("newestJZ", newestJZ);
		page.putField("newestJZDate", newestJZDate);
		
		//累计净值
		String leijiJZ = "";
		try {
			leijiJZ = doc.select("li:containsOwn(累计净值) > span")
					.first().text().trim();
			leijiJZ = leijiJZ.replace("*", "");
			leijiJZ = leijiJZ.replace("--","");
		} catch (NullPointerException e) {
		}
		page.putField("leijiJZ", leijiJZ);
		
		
		//成立日期
		String setupDateStr = "";
		Date setupDate = null;
		try {
			setupDateStr = doc.select("li:containsOwn(成立日期) > span")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		setupDateStr = setupDateStr.replace("--","");
		if(StringUtils.isNotEmpty(setupDateStr)) {
			try {
				setupDate = DateUtils.stringToDate(setupDateStr, "yyyy-MM-dd");
			} catch (Exception e) {
				setupDate = null;
			}
		}
		page.putField("setupDate", setupDate);
		
		//基金类型
		String fundType = "";
		try {
			fundType = doc.select("li:containsOwn(基金类型) > span")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		fundType = fundType.replace("--","");
		page.putField("fundType", fundType);
		
		//备案编号
		String recordNO = "";
		try {
			recordNO = doc.select("li:containsOwn(备案编号) > span")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		recordNO = recordNO.replace("--","").replace("无","");
		page.putField("recordNO", recordNO);
		
		//基金公司
		String fundCompany = "";
		String companyCode = "";
		try {
			fundCompany = doc.select("li:containsOwn(基金公司) > span > a")
					.first().text().trim();
			companyCode = doc.select("li:containsOwn(基金公司) > span > a")
					.first().attr("href").trim();
			if(StringUtils.isNotEmpty(companyCode)) {
				companyCode = companyCode.replace(".html","").replaceAll("/$", "");
				companyCode = companyCode.substring(companyCode.lastIndexOf("/") + 1);
			}
		} catch (NullPointerException e) {
		}
		fundCompany = fundCompany.replace("--","");
		page.putField("fundCompany", fundCompany);
		page.putField("companyCode", companyCode);
		
		//基金经理
		String fundManager = "";
		String managerCode = "";
		try {
			fundManager = doc.select("li:containsOwn(基金经理) > span > a")
					.first().text().trim();
			managerCode = doc.select("li:containsOwn(基金经理) > span > a")
					.first().attr("href").trim();
			if(StringUtils.isNotEmpty(managerCode)) {
				managerCode = managerCode.substring(managerCode.lastIndexOf("/") + 1)
					.replace(".html","");
			}
		} catch (NullPointerException e) {
		}
		fundManager = fundManager.replace("--","");
		page.putField("fundManager", fundManager);
		page.putField("managerCode", managerCode);
		
		//今年以来净值累计收益率
		String thisyearshouyilv = "";
		try {
			thisyearshouyilv = doc.select("li:containsOwn(今年以来) > span")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		thisyearshouyilv = thisyearshouyilv.replace("--","");
		page.putField("thisyearshouyilv", thisyearshouyilv);
		
		//成立以来净值累计收益率
		String aftersetupshouyilv = "";
		try {
			aftersetupshouyilv = doc.select("li:containsOwn(成立以来) > span")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		aftersetupshouyilv = aftersetupshouyilv.replace("--","");
		page.putField("aftersetupshouyilv", aftersetupshouyilv);
		
		//创收益评分
		String chuangshouyiScore = "";
		try {
			chuangshouyiScore = doc.select("span:containsOwn(创收益评分) + strong")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		chuangshouyiScore = chuangshouyiScore.replace("--","");
		page.putField("chuangshouyiScore", chuangshouyiScore);
		
		//抗风险评分
		String kangfengxianScore = "";
		try {
			kangfengxianScore = doc.select("span:containsOwn(抗风险评分) + strong")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		kangfengxianScore = kangfengxianScore.replace("--","");
		page.putField("kangfengxianScore", kangfengxianScore);
		
		//组织形式
		String orgType = "";
		try {
			orgType = doc.select("td:containsOwn(组织形式) + td")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		orgType = orgType.replace("--","");
		page.putField("orgType", orgType);
		
		//托管银行
		String tgBank = "";
		try {
			tgBank = doc.select("td:containsOwn(托管银行) + td")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		tgBank = tgBank.replace("--","");
		page.putField("tgBank", tgBank);
		
		//开放日期
		String openDate = "";
		try {
			openDate = doc.select("td:containsOwn(开放日期) + td")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		openDate = openDate.replace("--","");
		page.putField("openDate", openDate);
		
		//币种
		String currency = "";
		try {
			currency = doc.select("td:containsOwn(币种) + td")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		currency = currency.replace("--","");
		page.putField("currency", currency);
		
		//封闭期
		String fengbiqi = "";
		try {
			fengbiqi = doc.select("td:containsOwn(封闭期) + td")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		fengbiqi = fengbiqi.replace("--","");
		page.putField("fengbiqi", fengbiqi);
		
		//结构形式
		String jiegouXS = "";
		try {
			jiegouXS = doc.select("td:containsOwn(结构形式) + td")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		jiegouXS = jiegouXS.replace("--","");
		page.putField("jiegouXS", jiegouXS);
		
		//基金发行人
		String publishMan = "";
		try {
			publishMan = doc.select("td:containsOwn(基金发行人) + td")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		publishMan = publishMan.replace("--","");
		page.putField("publishMan", publishMan);
		
		//基金状态
		String fundStatus = "";
		try {
			fundStatus = doc.select("td:containsOwn(基金状态) + td")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		fundStatus = fundStatus.replace("--","");
		page.putField("fundStatus", fundStatus);
		
		//净值单位
		String jzUnit = "";
		try {
			jzUnit = doc.select("td:containsOwn(净值单位) + td")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		jzUnit = jzUnit.replace("--","");
		page.putField("jzUnit", jzUnit);
		
		//准封闭期
		String zhunfengbiqi = "";
		try {
			zhunfengbiqi = doc.select("td:containsOwn(准封闭期) + td")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		zhunfengbiqi = zhunfengbiqi.replace("--","");
		page.putField("zhunfengbiqi", zhunfengbiqi);
		
		//起购资金
		String qigouZJ = "";
		try {
			qigouZJ = doc.select("td:containsOwn(起购资金) + td")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		qigouZJ = qigouZJ.replace("--","");
		page.putField("qigouZJ", qigouZJ);
		
		//认购费率
		String rengouFL = "";
		try {
			rengouFL = doc.select("td:containsOwn(认购费率) + td")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		rengouFL = rengouFL.replace("--","");
		page.putField("rengouFL", rengouFL);
		
		//管理费率
		String manageFL = "";
		try {
			manageFL = doc.select("td:containsOwn(管理费率) + td")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		manageFL = manageFL.replace("--","");
		page.putField("manageFL", manageFL);
		
		//浮动管理费率
		String floatmanageFL = "";
		try {
			floatmanageFL = doc.select("td:containsOwn(浮动管理费率) + td")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		floatmanageFL = floatmanageFL.replace("--","");
		page.putField("floatmanageFL", floatmanageFL);
		
		//赎回费率
		String shuhuiFL = "";
		try {
			shuhuiFL = doc.select("td:containsOwn(赎回费率) + td")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		shuhuiFL = shuhuiFL.replace("--","");
		page.putField("shuhuiFL", shuhuiFL);
		
		//追加最低金额
		String appendMinJE = "";
		try {
			appendMinJE = doc.select("td:containsOwn(追加最低金额) + td")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		appendMinJE = appendMinJE.replace("--","");
		page.putField("appendMinJE", appendMinJE);
		
		//浮动酬提取说明
		String floatChou = "";
		try {
			floatChou = doc.select("td:containsOwn(浮动酬提取说明) + td")
					.first().text().trim();
		} catch (NullPointerException e) {
		}
		floatChou = floatChou.replace("--","");
		page.putField("floatChou", floatChou);
		
		//近一年收益率
		String nearyearshouyilv = null;
		double recentYearReturn = 0d;
		try {
			Elements tds = doc.select("div[id=nTab3_Con1] > table td:contains(收益率) ~ td");
			if(tds != null && tds.size() > 0) {
				nearyearshouyilv = tds.get(4).text().replace("--","").replace("%", "").trim();
			}
			if(nearyearshouyilv == null) {
				nearyearshouyilv = aftersetupshouyilv;
			}
		} catch (Exception e) {
			
		}
		if(StringUtils.isNotEmpty(nearyearshouyilv)) {
			try {
				recentYearReturn = Double.valueOf(nearyearshouyilv);
			} catch (Exception e) {
				recentYearReturn = 0d;
			}
		}
		page.putField("recentYearReturn", recentYearReturn);
	}
}
