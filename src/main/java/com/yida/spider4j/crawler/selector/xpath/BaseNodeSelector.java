package com.yida.spider4j.crawler.selector.xpath;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.yida.spider4j.crawler.selector.NodeSelector;
import com.yida.spider4j.crawler.selector.PlainTextSelector;
import com.yida.spider4j.crawler.utils.xml.XPathUtils;

/**
 * @ClassName: BaseNodeSelector
 * @Description: 节点选择器默认实现
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月30日 下午4:59:12
 *
 */
public abstract class BaseNodeSelector implements NodeSelector,PlainTextSelector {
	protected XPathUtils xpathUtils = XPathUtils.getInstance();
	
	@Override
    public String select(String text) {
        if (text != null && !"".equals(text)) {
        	return select(xpathUtils.fragmentToDocument(text));
        }
        return null;
    }

    @Override
    public List<String> selectList(String text) {
    	if (text != null && !"".equals(text)) {
    		return selectList(xpathUtils.fragmentToDocument(text));
        }
    	return new ArrayList<String>();
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: selectNode
     * @Description: 根据html代码片段过滤得到目标节点
     * @param @param text
     * @param @return
     * @return Node
     * @throws
     */
    public Node selectNode(String text) {
    	if (text != null && !"".equals(text)) {
    		//先根据html片段构建Document(Document为Node的子类)
			return selectNode(xpathUtils.fragmentToDocument(text));
        }
        return null;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: selectNodes
     * @Description: 根据html代码片段过滤得到目标节点集合
     * @param @param text
     * @param @return
     * @return List<Node>
     * @throws
     */
    public List<Node> selectNodes(String text) {
    	if (text != null && !"".equals(text)) {
    		return selectNodes(xpathUtils.fragmentToDocument(text));
        }
    	return null;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: selectNode
     * @Description: 根据一个节点过滤得到目标节点
     * @param @param node
     * @param @return
     * @return Node
     * @throws
     */
    public abstract Node selectNode(Node node);

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: selectNodes
     * @Description: 根据一个节点过滤得到目标节点集合
     * @param @param node
     * @param @return
     * @return List<Node>
     * @throws
     */
    public abstract List<Node> selectNodes(Node node);

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: hasAttribute
     * @Description: 节点是否包含属性
     * @param @return
     * @return boolean
     * @throws
     */
    public abstract boolean hasAttribute();
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: shouldMulti
     * @Description: 是否需要匹配多个节点
     * @param @return
     * @return boolean
     * @throws
     */
    public abstract boolean shouldMulti();
}
