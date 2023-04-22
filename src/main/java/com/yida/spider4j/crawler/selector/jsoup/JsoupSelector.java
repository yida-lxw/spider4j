package com.yida.spider4j.crawler.selector.jsoup;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupSelector extends BaseElementSelector {
	/**jsoup选择器表达式*/
	private String selectorText;
	
	/**属性名称*/
    private String attrName;
    
    /**是否匹配多个元素[默认为true,false表示只匹配第一个符合要求的元素，即使根据jsoup选择器表达式有多个元素符合]*/
    private boolean multi;

    public JsoupSelector(String selectorText) {
        this.selectorText = selectorText;
        this.multi = true;
    }

    public JsoupSelector(String selectorText, String attrName) {
        this.selectorText = selectorText;
        this.attrName = attrName;
    }
    
    public JsoupSelector(String selectorText, boolean multi) {
        this.selectorText = selectorText;
        this.multi = multi;
    }
    
    public JsoupSelector(String selectorText, String attrName,boolean multi) {
        this.selectorText = selectorText;
        this.attrName = attrName;
        this.multi = multi;
    }

    private String getValue(Element element) {
        if (attrName == null) {
            return element.outerHtml();
        } else if ("innerHtml".equalsIgnoreCase(attrName)) {
            return element.html();
        } else if ("outerHtml".equalsIgnoreCase(attrName)) {
            return element.outerHtml();
        } else if ("text".equalsIgnoreCase(attrName)) {
            return getText(element);
        } else if ("allText".equalsIgnoreCase(attrName)) {
            return element.text();
        } else {
            return element.attr(attrName);
        }
    }

    protected String getText(Element element) {
        if(null == element) {
        	return null;
        }
        return element.text();
    }

    @Override
    public String select(Element element) {
        List<Element> elements = selectElements(element);
        if (null == elements || elements.size() <= 0) {
            return null;
        }
        return getValue(elements.get(0));
    }

    @Override
    public List<String> selectList(Element doc) {
        List<String> strings = new ArrayList<String>();
        List<Element> elements = selectElements(doc);
        if (null != elements && elements.size() > 0) {
            for (Element element : elements) {
                String value = getValue(element);
                if (value != null) {
                    strings.add(value);
                }
            }
        }
        return strings;
    }

    @Override
    public Element selectElement(Element element) {
        Elements elements = element.select(selectorText);
        if (null != elements && elements.size() > 0) {
            return elements.get(0);
        }
        return null;
    }

    @Override
    public List<Element> selectElements(Element element) {
        return element.select(selectorText);
    }

    @Override
    public boolean hasAttribute() {
    	return null != attrName && !"".equals(attrName);
    }
    
    @Override
    public boolean shouldMulti() {
    	return this.multi;
    }

	public String getSelectorText() {
		return selectorText;
	}

	public void setSelectorText(String selectorText) {
		this.selectorText = selectorText;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public boolean isMulti() {
		return multi;
	}

	public void setMulti(boolean multi) {
		this.multi = multi;
	}
}
