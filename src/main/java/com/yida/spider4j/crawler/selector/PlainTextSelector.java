package com.yida.spider4j.crawler.selector;

import java.util.List;

/**
 * @ClassName: Selector
 * @Description: 文本选择器(基于字符串提取数据,比如XPath,正则表达式,JsonPath)
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月30日 上午9:56:15
 *
 */
public interface PlainTextSelector extends Selector {
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: select
	 * @Description: 根据选择器表达式提取数据
	 * @param @param text
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String select(String text);

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: selectList
	 * @Description: 根据选择器表达式提取数据,返回一个字符串集合
	 * @param @param text
	 * @param @return
	 * @return List<String>
	 * @throws
	 */
    public List<String> selectList(String text);
}
