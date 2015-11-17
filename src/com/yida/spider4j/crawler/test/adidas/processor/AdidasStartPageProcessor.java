package com.yida.spider4j.crawler.test.adidas.processor;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.processor.SimpleStartPageProcessor;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.selector.ExpressionType;
import com.yida.spider4j.crawler.selector.Selectable;
import com.yida.spider4j.crawler.utils.common.StringUtils;

/**
 * @ClassName: AdidasStartPageProcessor
 * @Description: 阿迪达斯起始页处理器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午2:40:10
 *
 */
public class AdidasStartPageProcessor extends SimpleStartPageProcessor {
	public AdidasStartPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: determineTotalPage
	 * @Description: 探测总页数
	 * @param @param page
	 * @param pagesize  每页显示大小
	 * @param @return
	 * @return int
	 * @throws
	 */
	@Override
	public int determineTotalPage(Page page, int pageSize) {
		if(page == null) {
			return 0;
		}
		String text = null;
		try {
			//FileUtils.writeFile(page.getRawText(), "C:/movie.html", "UTF-8", false);
			text = page.getHtml().jsoup("div[class=pdPagination fr] > span[class=fl]").get();
		} catch (Exception e) {
			return 0;
		}
		if(StringUtils.isEmpty(text)) {
			return 0;
		}
		text = StringUtils.getNumberFromString(text);
		if(StringUtils.isEmpty(text)) {
			return 0;
		}
		try {
			return Integer.valueOf(text);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: determinePageSize
	 * @Description: 探测每页显示大小
	 * @param @param page
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int determinePageSize(Page page) {
		return 24;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: knownTotalPage
	 * @Description: 告诉PageProcessor,从已有的分页页面内容可否已知总页数
	 *               默认实现为true,若你不清楚默认实现是什么,最好是自己重写一下
	 *           
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	@Override
	public boolean knownTotalPage() {
		return true;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: needPaging
	 * @Description: 告诉PageProcessor,是否需要分页
	 *               默认实现为true,若你不清楚默认实现是什么,最好是自己重写一下
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean needPaging() {
		return true;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: buildNextPageUrl
	 * @Description: 构建下一页的请求URL
	 * @param @param page
	 * @param @param currentPage
	 * @param @param totalPage
	 * @param @param pageSize
	 * @param @return
	 * @return String
	 * @throws
	 */
	@Override
	public String buildNextPageUrl(Page page, int currentPage, int totalPage,
			int pageSize) {
		Selectable selectable = page.getHtml(ExpressionType.JSOUP)
			.jsoup("a[class=sizeOption fl selected]", "text");
		if(selectable == null) {
			return null;
		}
		String pageUrl = selectable.get();
		if(StringUtils.isEmpty(pageUrl)) {
			return null;
		}
		pageUrl = StringUtils.getURLFromString(pageUrl);
		pageUrl = pageUrl + "&limit=" + pageSize + "&p=" + currentPage;
		//System.out.println("next-page-Url:" + pageUrl);
		return pageUrl;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: needPaging
	 * @Description: 告诉PageProcessor,当前页是第几页
	 * @param page
	 * @param pageSize  每页显示大小
	 * @return int  返回当前页的页码,页码从1开始计算
	 * @throws
	 */
	@Override
	public int judgeCurrentPage(Page page,int pageSize) {
		if(page == null) {
			return -1;
		}
		String pageUrl = page.getRequest().getUrl();
		String pos = "&p=";
		pageUrl = pageUrl.substring(pageUrl.lastIndexOf(pos) + pos.length());
		try {
			return Integer.valueOf(pageUrl);
		} catch (NumberFormatException e) {
			return 1;
		}
	}
	
	
}
