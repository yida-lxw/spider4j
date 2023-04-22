package com.yida.spider4j.crawler.processor;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;

/**
 * @ClassName: ListPageProcessor
 * @Description: 列表页PageProcessor实现
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月13日 下午3:42:47
 *
 */
public abstract class ListPageProcessor extends AbstractMultiPageProcessor {
	public ListPageProcessor(PageProcessorParam pageProcessorParam) {
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
	public int determineTotalPage(Page page,int pageSize) {
		return -1;
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
		return -1;
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
}
