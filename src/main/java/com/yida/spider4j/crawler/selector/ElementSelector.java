package com.yida.spider4j.crawler.selector;

import java.util.List;

import org.jsoup.nodes.Element;

/**
 * @ClassName: ElementSelector
 * @Description: Jsoup Element选择器(基于元素提取数据)
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月30日 下午4:35:53
 *
 */
public interface ElementSelector extends Selector {
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: select
	 * @Description: 从指定的Element中提取文本
	 * @param @param element
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String select(Element element);
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: selectList
	 * @Description: 从指定的Element中提取多段文本
	 * @param @param element
	 * @param @return
	 * @return List<String>
	 * @throws
	 */
	public List<String> selectList(Element element);
}
