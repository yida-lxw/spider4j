package com.yida.spider4j.crawler.selector;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.Node;

import com.yida.spider4j.crawler.selector.jsoup.BaseElementSelector;
import com.yida.spider4j.crawler.selector.jsoup.JsoupSelector;
import com.yida.spider4j.crawler.selector.xpath.BaseNodeSelector;
import com.yida.spider4j.crawler.selector.xpath.XpathSelector;
import com.yida.spider4j.crawler.utils.common.StringUtils;

/**
 * @ClassName: HtmlNode
 * @Description: HTML节点
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月8日 下午5:52:24
 *
 */
public class HtmlNode extends AbstractSelectable {

    protected List<Element> elements;
    
    protected List<Node> nodes;

    public HtmlNode() {
        this.elements = null;
        this.nodes = null;
    }
    
    public HtmlNode(List<Element> elements) {
        this.elements = elements;
        this.nodes = null;
    }

    public HtmlNode(List<Node> nodes,List<Element> elements) {
    	this.nodes = nodes;
    	this.elements = elements;
    }

    protected Selectable selectElements(Selector selector) {
    	//jsoup方式
    	if(selector instanceof BaseElementSelector && null != getElements() && getElements().size() > 0) {
    		ListIterator<Element> elementIterator = getElements().listIterator();
    		BaseElementSelector baseElementSelector = (BaseElementSelector)selector;
            //如果没有指定属性,则默认会构建Element or Node
    		if (!baseElementSelector.hasAttribute()) {
                List<Element> resultElements = new ArrayList<Element>();
                while (elementIterator.hasNext()) {
                    Element element = checkElementAndConvert(elementIterator);
                    // 若需要匹配多个元素
                    if(baseElementSelector.shouldMulti()) {
                    	List<Element> selectElements = baseElementSelector.selectElements(element);
                    	if(selectElements != null && selectElements.size() > 0) {
                            resultElements.addAll(selectElements);
                    	}
                    } 
                    // 不管是否匹配到多个元素,始终只提取匹配到的第一个元素
                    else {
                    	Element selectElement = baseElementSelector.selectElement(element);
                    	if(selectElement != null) {
                            resultElements.add(selectElement);
                    	}
                    }
                }
                return new HtmlNode(resultElements);
            } else {
            	//有指定属性,则认为需要转为文本节点
                List<String> resultStrings = new ArrayList<String>();
                while (elementIterator.hasNext()) {
                    Element element = checkElementAndConvert(elementIterator);
                    if(baseElementSelector.shouldMulti()) {
                    	List<String> selectList = baseElementSelector.selectList(element);
                    	if(selectList != null && selectList.size() > 0) {
                    		resultStrings.addAll(selectList);
                    	}
                    } else {
                    	String selectResult = baseElementSelector.select(element);
                    	if(StringUtils.isNotEmpty(selectResult)) {
                        	resultStrings.add(selectResult);
                    	}
                    }
                }
                return new PlainText(resultStrings);
            }
    	} 
    	// Xpath方式
    	else if(selector instanceof BaseNodeSelector && null != getNodes() && getNodes().size() > 0) {
    		ListIterator<Node> nodeIterator = getNodes().listIterator();
    		BaseNodeSelector baseNodeSelector = (BaseNodeSelector)selector;
            if (!baseNodeSelector.hasAttribute()) {
                List<Node> resultNodes = new ArrayList<Node>();
                while (nodeIterator.hasNext()) {
                	// 若需要匹配多个元素
                    if(baseNodeSelector.shouldMulti()) {
                    	List<Node> selectNodes = baseNodeSelector.selectNodes(nodeIterator.next());
                    	if(selectNodes != null && selectNodes.size() > 0) {
                    		resultNodes.addAll(selectNodes);
                    	}
                    } else {
                    	Node selectNode = baseNodeSelector.selectNode(nodeIterator.next());
                    	if(null != selectNode) {
                    		resultNodes.add(selectNode);
                    	}
                    }
                }
                return new HtmlNode(resultNodes,null);
            } else {
                List<String> resultStrings = new ArrayList<String>();
                while (nodeIterator.hasNext()) {
                	// 若需要匹配多个元素
                    if(baseNodeSelector.shouldMulti()) {
                    	List<String> selectList = baseNodeSelector.selectList(nodeIterator.next());
                    	if(selectList != null && selectList.size() > 0) {
                    		resultStrings.addAll(selectList);
                    	}
                    } else {
                    	String selectResult = baseNodeSelector.select(nodeIterator.next());
                    	if(StringUtils.isNotEmpty(selectResult)) {
                    		resultStrings.add(selectResult);
                    	}
                    }
                }
                return new PlainText(resultStrings);
            }
    	}
    	return null; 
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: checkElementAndConvert
     * @Description: 检查Element对象,若不是Document类型则转成Document,
     *               因为对于jsoup,只有Document才能select
     * @param elementIterator
     * @param @return
     * @return Element
     * @throws
     */
    private Element checkElementAndConvert(ListIterator<Element> elementIterator) {
        Element element = elementIterator.next();
        if (!(element instanceof Document)) {
            Document root = new Document(element.ownerDocument().baseUri());
            Element clone = element.clone();
            root.appendChild(clone);
            elementIterator.set(root);
            return root;
        }
        return element;
    }
    
    protected List<Element> getElements() {
        return this.elements;
    }
    
    protected List<Node> getNodes() {
        return this.nodes;
    }

    @Override
    public Selectable smartContent() {
        SmartContentSelector smartContentSelector = Selectors.smartContent();
        return select(smartContentSelector, getSourceTexts());
    }

    @Override
    public Selectable links() {
        return xpath("//a/@href");
    }

    @Override
    public Selectable selectList(Selector selector) {
    	if(selector instanceof PlainTextSelector) {
    		return selectList(selector, getSourceTexts());
        }
    	if (selector instanceof ElementSelector || selector instanceof Selector) {
           return selectElements(selector);
        }
        return null;
    }

    @Override
    public Selectable select(Selector selector) {
        return selectList(selector);
    }

    @Override
    public List<Selectable> nodes() {
        List<Selectable> selectables = new ArrayList<Selectable>();
        if(null != getElements() && getElements().size() > 0) {
        	for (Element element : getElements()) {
                List<Element> childElements = new ArrayList<Element>(1);
                childElements.add(element);
                selectables.add(new HtmlNode(childElements));
            }
        } else if(null != getNodes() && getNodes().size() > 0) {
        	for (Node node : getNodes()) {
                List<Node> childNodes = new ArrayList<Node>(1);
                childNodes.add(node);
                selectables.add(new HtmlNode(childNodes,null));
            }
        }
        return selectables;
    }

    @Override
    protected List<String> getSourceTexts() {
    	if(getElements() == null || getElements().size() <= 0) {
    		List<String> sourceTexts = new ArrayList<String>(getNodes().size());
            for (Node node : getNodes()) {
                sourceTexts.add(node.getNodeValue());
            }
            return sourceTexts;
    	} else {
    		List<String> sourceTexts = new ArrayList<String>(getElements().size());
            for (Element element : getElements()) {
                sourceTexts.add(element.toString());
            }

            return sourceTexts;
    	}
    }

	@Override
	public Selectable jsoup(String jsoupExpression) {
		JsoupSelector jsoupSelector = Selectors.jsoup(jsoupExpression);
        return selectElements(jsoupSelector);
	}

	@Override
	public Selectable jsoup(String jsoupExpression, String attrName) {
		JsoupSelector jsoupSelector = Selectors.jsoup(jsoupExpression,attrName);
        return selectElements(jsoupSelector);
	}
	
	@Override
	public Selectable jsoup(String jsoupExpression,boolean multi) {
		JsoupSelector jsoupSelector = Selectors.jsoup(jsoupExpression, multi);
		return selectElements(jsoupSelector);
	}

	@Override
	public Selectable jsoup(String jsoupExpression, String attrName,boolean multi) {
		JsoupSelector jsoupSelector = Selectors.jsoup(jsoupExpression, attrName, multi);
		return selectElements(jsoupSelector);
	}

	@Override
    public Selectable xpath(String xpath) {
        XpathSelector xpathSelector = Selectors.xpath(xpath);
        return selectElements(xpathSelector);
    }
    
    @Override
	public Selectable xpath(String xpath, boolean multi) {
    	XpathSelector xpathSelector = Selectors.xpath(xpath,multi);
        return selectElements(xpathSelector);
	}

	
	@Override
	public Selectable xpath(String xpath, String attrName) {
    	XpathSelector xpathSelector = Selectors.xpath(xpath,attrName);
        return selectElements(xpathSelector);
	}

	@Override
	public Selectable xpath(String xpath, String attrName, boolean multi) {
    	XpathSelector xpathSelector = Selectors.xpath(xpath,attrName,multi);
        return selectElements(xpathSelector);
	}
}
