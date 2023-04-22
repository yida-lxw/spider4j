package com.yida.spider4j.crawler.selector;

import java.util.List;

import org.w3c.dom.Node;

/**
 * @ClassName: NodeSelector
 * @Description: W3C Document Node选择器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月30日 下午4:37:40
 *
 */
public interface NodeSelector extends Selector {
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: select
	 * @Description: 从W3C Document节点中提取文本
	 * @param @param node
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String select(Node node);
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: selectList
	 * @Description: 从W3C Document节点中提取多段文本
	 * @param @param node
	 * @param @return
	 * @return List<String>
	 * @throws
	 */
	public List<String> selectList(Node node);
}
