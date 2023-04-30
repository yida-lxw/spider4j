package com.yida.spider4j.crawler.test.wangyi;

import com.yida.spider4j.crawler.core.Spider;
import com.yida.spider4j.crawler.pipeline.ConsolePipeline;
import com.yida.spider4j.crawler.processor.param.AttachedPageProcessorParam;
import com.yida.spider4j.crawler.processor.param.DetailPageProcessorParam;
import com.yida.spider4j.crawler.processor.param.MultiPageProcessorParam;
import com.yida.spider4j.crawler.test.wangyi.pageprocessor.WangyiAttachedPageProcessor;
import com.yida.spider4j.crawler.test.wangyi.pageprocessor.WangyiDetailPageProcessor;
import com.yida.spider4j.crawler.test.wangyi.pageprocessor.WangyiListPageProcessor;
import com.yida.spider4j.crawler.test.wangyi.pageprocessor.WangyiStartPageProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: DoubanSpider
 * @Description: 豆瓣电影爬虫测试类
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午7:14:02
 *
 */
public class WangyiSpider {
	public static void main(String[] args) {
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("accept-language", "zh-CN,zh;q=0.9,en;q=0.8");
		headerMap.put("cache-control", "max-age=0");
		headerMap.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
		headerMap.put("cookie", "_ntes_nnid=c4829ad26814fb4fff22abb831dc934b,1645542203595; _ntes_nuid=c4829ad26814fb4fff22abb831dc934b; P_INFO=\"chatgpt_yida@126.com|1680685282|0|unireg|00&99|null&null&null#chq&null#10#0#0|&0||chatgpt_yida@126.com\"; __bid_n=1878ac89484f9eb77f4207; FPTOKEN=MbJIV/mbLFbhfgQP/f1cySLFnmhX2KEy4HZyOD2PDn6eFyxHzFDuIJGUX3p/hM/5/4jCL7W2lzWxrNxgxCJgWSfp/u4wjyhQULYcI5uTu0dRFTgCdRi/7xT1R1GhOt2ItfG9zX4ruRHdc13llFEm6MWFEhf5W9pp9AQfcO9ezMrUdTAzUeCMyOFeMI+QvlFsYnXWbB5aA00A9MYEQrZ5xsWeqDBPImnk9NU+TEt2byyvvrowgQNpNn2hNK/O+r6H7415ls3vJYGIy5TvotsiCYQZh1Em5jL8IzpdQbu/KsI/kMgpNnB57FbKqnqT0mCHEeo1Bw7SBOgH3apumfVKT11S3uRRTmYsS2bFrHgP4ZRbU0xh2fAXoRY/X/5Ec3hSJA/r61ZYxI7LhFOYDI6lLw==|twyy4HLzbF1KfFv+GcAERii+vowXDNOtHhGvqt7GYWI=|10|adce63bbfdb0bece45539fd7dc9c16b4; pver_n_f_l_n3=a; ne_analysis_trace_id=1682848318720; s_n_f_l_n3=684915a768932bb71682848318722; _antanalysis_s_id=1682848319127; UserProvince=%u5168%u56FD; vinfo_n_f_l_n3=684915a768932bb7.1.17.1662353869926.1682846412690.1682849563970");
		headerMap.put("sec-ch-ua", "\"Chromium\";v=\"112\", \"Google Chrome\";v=\"112\", \"Not:A-Brand\";v=\"99\"");
		headerMap.put("sec-ch-ua-mobile", "?0");
		headerMap.put("sec-ch-ua-platform", "macOS");
		headerMap.put("sec-fetch-dest", "document");
		headerMap.put("sec-fetch-mode", "navigate");
		headerMap.put("sec-fetch-site", "none");
		headerMap.put("sec-fetch-user", "?1");
		headerMap.put("upgrade-insecure-requests", "1");
		headerMap.put("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36");

		//start
		String startUrlTemplate = "https://news.163.com/special/cm_sports_%s/?callback=data_callback";
		List<String> startUrlList = new ArrayList<>();
		for(int i=2; i < 11; i++) {
			String num = "";
			if(i < 10)  {
				num = "0" + String.valueOf(i);
			} else {
				num = i + "";
			}
			String url = String.format(startUrlTemplate, num);
			startUrlList.add(url);
		}
		startUrlList.add("https://news.163.com/special/cm_guonei_02/?callback=data_callback");
		startUrlList.add("https://news.163.com/special/cm_guonei_03/?callback=data_callback");
		startUrlList.add("https://news.163.com/special/cm_guoji_02/?callback=data_callback");
		startUrlList.add("https://news.163.com/special/cm_war_02/?callback=data_callback");
		MultiPageProcessorParam startPageProcessorParam = new MultiPageProcessorParam();
		WangyiStartPageProcessor startPageProcessor = new WangyiStartPageProcessor(startPageProcessorParam);


				
		//list
		MultiPageProcessorParam listPageProcessorParam = new MultiPageProcessorParam();
		//listPageProcessorParam.setTargetUrlJsoup("div[class=ndi_main] > div[class^=data_row] > a[class=na_pic]");

		//获取href属性值
		listPageProcessorParam.setAttributeName("href");
		WangyiListPageProcessor listPageProcessor = new WangyiListPageProcessor(listPageProcessorParam);
		
		//detail
		DetailPageProcessorParam detailPageProcessorParam = new DetailPageProcessorParam();
		WangyiDetailPageProcessor detailPageProcessor = new WangyiDetailPageProcessor(detailPageProcessorParam);

		AttachedPageProcessorParam attachedPageProcessorParam = new AttachedPageProcessorParam();
		WangyiAttachedPageProcessor attachedPageProcessor = new WangyiAttachedPageProcessor(attachedPageProcessorParam);

		Spider.create(startPageProcessor,listPageProcessor, detailPageProcessor, attachedPageProcessor)
				.startUrls(startUrlList)
			.addPipeline(new ConsolePipeline())
			.threadNum(10)
			.start();
		
	}

}
