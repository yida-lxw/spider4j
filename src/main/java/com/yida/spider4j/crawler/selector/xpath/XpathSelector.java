package com.yida.spider4j.crawler.selector.xpath;

import java.util.List;

import org.w3c.dom.Node;

/**
 * @ClassName: XpathSelector
 * @Description: XPath选择器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月30日 下午4:42:14
 *
 */
public class XpathSelector extends BaseNodeSelector {
	/**xpath表达式*/
    private String xpath;
    
    /**属性名称*/
    private String attributeName;
    
    /**是否匹配多个节点[默认为true,false表示只匹配第一个符合要求的节点，即使根据xpath表达式有多个节点符合]*/
    private boolean multi;

    public XpathSelector(String xpath) {
        this.xpath = xpath;
        this.multi = true;
    }
    
    public XpathSelector(String xpath,String attributeName) {
        this.xpath = xpath;
        this.attributeName = attributeName;
    }
    
    public XpathSelector(String xpath,boolean multi) {
        this.xpath = xpath;
        this.multi = multi;
    }
    
    public XpathSelector(String xpath,String attributeName,boolean multi) {
        this.xpath = xpath;
        this.attributeName = attributeName;
        this.multi = multi;
    }

	@Override
	public String select(Node node) {
		if(this.attributeName != null && !"".equals(this.attributeName)) {
			return xpathUtils.getNodeAttributeValue(node, this.xpath, this.attributeName);
		}
		//attribute::id 选取节点的id属性
		return xpathUtils.getNodeText(node, this.xpath);
	}

	@Override
	public List<String> selectList(Node node) {
		if(this.attributeName != null && !"".equals(this.attributeName)) {
			if("text".equalsIgnoreCase(this.attributeName)) {
				return xpathUtils.getMultiNodeText(node, this.xpath);
			}
			return xpathUtils.getMultiNodeAttributeValue(node, this.xpath, this.attributeName);
		}
		return xpathUtils.getMultiNodeText(node, this.xpath);
	}

	@Override
	public Node selectNode(Node node) {
		return xpathUtils.selectSingleNode(node, this.xpath);
	}

	@Override
	public List<Node> selectNodes(Node node) {
		return xpathUtils.selectNodeList(node, this.xpath);
	}

	@Override
	public boolean hasAttribute() {
		return null != attributeName && !"".equals(attributeName);
	}
	
	@Override
	public boolean shouldMulti() {
		return this.multi;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public boolean isMulti() {
		return multi;
	}

	public void setMulti(boolean multi) {
		this.multi = multi;
	}
}
