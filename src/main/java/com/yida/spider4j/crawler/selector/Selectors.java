package com.yida.spider4j.crawler.selector;

import com.yida.spider4j.crawler.selector.jsoup.JsoupSelector;
import com.yida.spider4j.crawler.selector.regex.RegexSelector;
import com.yida.spider4j.crawler.selector.xpath.XpathSelector;
/**
 * @ClassName: Selectors
 * @Description: 选择器工具类
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月8日 下午4:58:50
 *
 */
public class Selectors {
	public static RegexSelector regex(String expr) {
		return new RegexSelector(expr);
	}

	public static RegexSelector regex(String expr, int group) {
		return new RegexSelector(expr, group);
	}

	public static SmartContentSelector smartContent() {
		return new SmartContentSelector();
	}

	public static JsoupSelector jsoup(String expr) {
		return new JsoupSelector(expr);
	}

	public static JsoupSelector jsoup(String expr, String attrName) {
		return new JsoupSelector(expr, attrName);
	}
	
	public static JsoupSelector jsoup(String expr, boolean multi) {
		return new JsoupSelector(expr, multi);
	}
	
	public static JsoupSelector jsoup(String expr, String attrName,boolean multi) {
		return new JsoupSelector(expr, attrName,multi);
	}

	public static XpathSelector xpath(String expr) {
		return new XpathSelector(expr);
	}
	
	public static XpathSelector xpath(String expr, String attrName) {
		return new XpathSelector(expr,attrName);
	}
	
	public static XpathSelector xpath(String expr, boolean multi) {
		return new XpathSelector(expr,multi);
	}
	
	public static XpathSelector xpath(String expr, String attrName, boolean multi) {
		return new XpathSelector(expr,attrName,multi);
	}

	public static AndSelector and(Selector[] selectors) {
		return new AndSelector(selectors);
	}

	public static OrSelector or(Selector[] selectors) {
		return new OrSelector(selectors);
	}
}
