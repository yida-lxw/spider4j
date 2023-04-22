package com.yida.spider4j.crawler.selector.jsoup;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import com.yida.spider4j.crawler.selector.ElementSelector;
import com.yida.spider4j.crawler.selector.PlainTextSelector;

/**
 * @ClassName: BaseElementSelector
 * @Description: 元素选择器默认实现
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月30日 下午4:59:12
 *
 */
public abstract class BaseElementSelector implements ElementSelector,PlainTextSelector {
	@Override
    public String select(String text) {
        if (text != null && !"".equals(text)) {
            return select(Jsoup.parse(text));
        }
        return null;
    }

    @Override
    public List<String> selectList(String text) {
    	if (text != null && !"".equals(text)) {
            return selectList(Jsoup.parse(text));
        } else {
            return new ArrayList<String>();
        }
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: selectElement
     * @Description: 根据html代码片段过滤得到目标元素
     * @param @param text
     * @param @return
     * @return Element
     * @throws
     */
    public Element selectElement(String text) {
    	if (text != null && !"".equals(text)) {
    		//先根据html片段构建Document(Document为Element的子类)
            return selectElement(Jsoup.parse(text));
        }
        return null;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: selectElements
     * @Description: 根据html代码片段过滤得到目标元素集合
     * @param @param text
     * @param @return
     * @return List<Element>
     * @throws
     */
    public List<Element> selectElements(String text) {
    	if (text != null && !"".equals(text)) {
            return selectElements(Jsoup.parse(text));
        } else {
            return new ArrayList<Element>();
        }
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: selectElement
     * @Description: 根据一个元素过滤得到目标元素
     * @param @param element
     * @param @return
     * @return Element
     * @throws
     */
    public abstract Element selectElement(Element element);

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: selectElements
     * @Description: 根据一个元素过滤得到目标元素集合
     * @param @param element
     * @param @return
     * @return List<Element>
     * @throws
     */
    public abstract List<Element> selectElements(Element element);

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: hasAttribute
     * @Description: 元素是否包含属性
     * @param @return
     * @return boolean
     * @throws
     */
    public abstract boolean hasAttribute();
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: shouldMulti
     * @Description: 是否需要匹配多个元素
     * @param @return
     * @return boolean
     * @throws
     */
    public abstract boolean shouldMulti();
}
