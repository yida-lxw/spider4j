package com.yida.spider4j.crawler.test.wangyi.pageprocessor;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.core.PageType;
import com.yida.spider4j.crawler.core.Request;
import com.yida.spider4j.crawler.processor.SimpleDetailPageProcessor;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.selector.ExpressionType;
import com.yida.spider4j.crawler.utils.HttpConstant;
import com.yida.spider4j.crawler.utils.common.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: DoubanDetailPageProcessor
 * @Description: 网易新闻详情页处理器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午2:40:10
 *
 */
public class WangyiDetailPageProcessor extends SimpleDetailPageProcessor {
	private static final String commentCountUrlTemplate = "https://comment.api.163.com/api/v1/products/%s/threads/%s?ibc=jssdk";

	public WangyiDetailPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
	}

	@Override
	public void process(Page page) {
		super.process(page);
		String pageId = page.getHtml(ExpressionType.JSOUP).jsoup("div[id=contain]","data-docid").get();
		page.putField("pageId", pageId);

		page.putField("media",
				page.getHtml(ExpressionType.JSOUP).jsoup("div[class=date-source] > a[class^=source],span.source.ent-source," +
						"div[class=post_info] > a:first-child").get());

		page.putField("title",
				page.getHtml(ExpressionType.JSOUP).jsoup("h1[class=main-title],h1[class=post_title]").get());

		page.putField("publishDate", page.getHtml(ExpressionType.JSOUP).jsoup("span[class=ptime],span[class=post_info]").get());

		page.putField("commentCount", page.getHtml(ExpressionType.JSOUP).jsoup("a[class^=post_top_tie_count]").get());


		//FileUtils.writeFile(movieName + "\n", "C:/movie.txt", "UTF-8", true);
	}

	@Override
	public void buildExtraRequest(Page page) {
		String commentCountUrl = generateAttachedPageURL(page);
		if (StringUtils.isEmpty(commentCountUrl)) {
			return;
		}
		Request request = new Request(commentCountUrl, PageType.ATTACHED_PAGE, HttpConstant.Method.GET);
		page.addTargetRequest(request);
	}

	@Override
	public String generateAttachedPageURL(Page page) {
		String html = page.getHtml(ExpressionType.JSOUP).get();
		//获取productKey
		Pattern pattern = Pattern.compile("\"productKey\":\\s*\"(.*?)\"");
		Matcher matcher = pattern.matcher(html);
		String commentCountUrl = "";
		if (matcher.find()) {
			String productKey = matcher.group(1);
			if(null != productKey && !"".equals(productKey)) {
				pattern = Pattern.compile("\"docId\":\\s*\"(.*?)\"");
				matcher = pattern.matcher(html);
				if (matcher.find()) {
					String pageId = matcher.group(1);
					commentCountUrl = String.format(commentCountUrlTemplate, productKey, pageId);
				}
			}
		}
		return commentCountUrl;
	}
}
