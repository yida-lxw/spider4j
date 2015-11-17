package com.yida.spider4j.crawler.test.douban.pageprocessor;

import java.util.List;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.processor.SimpleStartPageProcessor;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.selector.Selectable;
import com.yida.spider4j.crawler.utils.Constant;
import com.yida.spider4j.crawler.utils.common.StringUtils;

/**
 * @ClassName: DoubanStartPageProcessor
 * @Description: 豆瓣电影Top250起始页处理器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午2:40:10
 *
 */
public class DoubanStartPageProcessor extends SimpleStartPageProcessor {
	public DoubanStartPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: determineTotalPage
	 * @Description: 探测总页数
	 * @param page
	 * @param pagesize  每页显示大小
	 * @param @return
	 * @return int
	 * @throws
	 */
	@Override
	public int determineTotalPage(Page page,int pageSize) {
		if(page == null) {
			return 0;
		}
		String text = page.getHtml().jsoup("span[class=count]").get();
		if(StringUtils.isEmpty(text)) {
			return 0;
		}
		text = StringUtils.getNumberFromString(text);
		if(StringUtils.isEmpty(text)) {
			return 0;
		}
		try {
			int total = Integer.valueOf(text);
			if(pageSize <= 0) {
				pageSize = Constant.PAGE_SIZE;
			}
			return (total % pageSize == 0) ? (total / pageSize) : (total / pageSize) + 1;
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
	@Override
	public int determinePageSize(Page page) {
		if(page == null) {
			return Constant.PAGE_SIZE;
		}
		List<Selectable> list = page.getHtml().jsoup("ol[class=grid_view] > li").nodes();
		if(null == list || list.size() <= 0) {
			return Constant.PAGE_SIZE;
		}
		return list.size();
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: knownTotalPage
	 * @Description: 告诉PageProcessor,从已有的分页页面内容可否已知总页数
	 *               需要用户实现
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
		int start = (currentPage - 1) * pageSize;
		String urlPrefix = "http://movie.douban.com/top250?start=0&filter=&type=";
		urlPrefix = urlPrefix.replace("?start=0", "?start=" + start);
		return urlPrefix;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: needPaging
	 * @Description: 告诉PageProcessor,列表页是否需要分页
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	@Override
	public boolean needPaging() {
		return true;
	}
}
