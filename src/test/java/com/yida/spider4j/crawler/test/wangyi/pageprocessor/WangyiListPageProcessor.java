package com.yida.spider4j.crawler.test.wangyi.pageprocessor;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.core.PageType;
import com.yida.spider4j.crawler.processor.SimpleListPageProcessor;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: DoubanDetailPageProcessor
 * @Description: 豆瓣电影Top250列表页处理器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午2:40:10
 *
 */
public class WangyiListPageProcessor extends SimpleListPageProcessor {
	private static final String commentCountUrlTemplate = "https://comment.api.163.com/api/v1/products/%s/threads/%s?ibc=jssdk";

	public WangyiListPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
	}

	@Override
	public List<String> pickUpTargetUrl(Page page) {
		String html = page.getHtml().get();
		//获取productKey
		Pattern pattern = Pattern.compile("\"productKey\":\\s*\"(.*?)\"");
		Matcher matcher = pattern.matcher(html);
		List<String> urlList = new ArrayList<>();
		if (matcher.find()) {
			String productKey = matcher.group(1);
			if(null != productKey && !"".equals(productKey)) {
				pattern = Pattern.compile("\"docId\":\\s*\"(.*?)\"");
				matcher = pattern.matcher(html);
				if (matcher.find()) {
					String pageId = matcher.group(1);
					String commentCountUrl = String.format(commentCountUrlTemplate, productKey, pageId);
					urlList.add(commentCountUrl);
				}
			}
		}
		return urlList;
	}

	@Override
	public void buildRequest(Page page) {
		buildTargetRequest(page, PageType.ATTACHED_PAGE);
	}
}
