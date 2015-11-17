package com.yida.spider4j.crawler.selector;

import java.util.ArrayList;
import java.util.List;

import com.yida.spider4j.crawler.selector.jsoup.BaseElementSelector;
import com.yida.spider4j.crawler.selector.regex.RegexSelector;
import com.yida.spider4j.crawler.selector.regex.ReplaceSelector;
import com.yida.spider4j.crawler.selector.xpath.BaseNodeSelector;
import com.yida.spider4j.crawler.utils.xml.XPathUtils;

/**
 * @ClassName: AbstractSelectable
 * @Description: Selectable默认实现
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月30日 下午2:16:36
 *
 */
public abstract class AbstractSelectable implements Selectable {
	protected XPathUtils xpathUtils = XPathUtils.getInstance();
	
	protected abstract List<String> getSourceTexts();
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: regex
     * @Description: 正则表达式提取
     * @param @param regex
     * @param @param group
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable regex(String regex, int group) {
    	RegexSelector regexSelector = Selectors.regex(regex,group);
        return selectList(regexSelector, getSourceTexts());
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: regex
     * @Description: 正则表达式提取
     * @param @param regex
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable regex(String regex) {
    	RegexSelector regexSelector = Selectors.regex(regex);
        return selectList(regexSelector, getSourceTexts());
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: jsonPath
     * @Description: JsonPath表达式提取[默认只返回匹配到的第一个]
     * @param @param jsonPath
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable jsonPath(String jsonPath) {
    	throw new UnsupportedOperationException("this function about selecting by Google's jsonpath is not yet implemented.");
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: jsonPath
     * @Description: JsonPath表达式提取
     * @param jsonPath
     * @param multi    是否返回多个匹配项
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable jsonPath(String jsonPath,boolean multi) {
    	throw new UnsupportedOperationException("this function about selecting by Google's jsonpath is not yet implemented.");
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: replace
     * @Description: 根据一个正则表达式替换
     * @param @param regex
     * @param @param replacement
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable replace(String regex, String replacement) {
    	ReplaceSelector replaceSelector = new ReplaceSelector(regex,replacement);
        return select(replaceSelector, getSourceTexts());
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: toString
     * @Description: toString
     * @param @return
     * @return String
     * @throws
     */
    public String toString() {
    	return get();
    }
    
    @Override
    public String get() {
    	List<String> list = all();
        if (list != null && list.size() > 0) {
            return all().get(0);
        }
        return null;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: match
     * @Description: 是否匹配到数据
     * @param @return
     * @return boolean
     * @throws
     */
    public boolean match() {
    	return getSourceTexts() != null && getSourceTexts().size() > 0;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: all
     * @Description: 返回匹配到的多个元素的文本
     * @param @return
     * @return List<String>
     * @throws
     */
    public List<String> all() {
    	return getSourceTexts();
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: select
     * @Description: 根据选择器提取数据
     * @param @param selector
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable select(Selector selector) {
    	return select(selector, getSourceTexts());
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: selectList
     * @Description: 根据选择器提取数据,支持多条
     * @param @param selector
     * @param @return
     * @return Selectable
     * @throws
     */
    public Selectable selectList(Selector selector) {
    	return selectList(selector, getSourceTexts());
    }
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: select
	 * @Description: 根据指定的选择器对多段文本进行提取
	 * @param @param selector
	 * @param @param strings
	 * @param @return
	 * @return Selectable
	 * @throws
	 */
	protected Selectable select(Selector selector, List<String> strings) {
        List<String> results = new ArrayList<String>();
        if(selector instanceof PlainTextSelector) {
    		PlainTextSelector selectorTemp = (PlainTextSelector)selector;
            for (String string : strings) {
                String result = selectorTemp.select(string);
                if(result == null) {
                	continue;
                }
                /*if(selector instanceof JsonPathSelector) {
                	if(result.startsWith("[") && result.endsWith("]")) {
                		result = result.replaceAll("^\\[", "")
                			.replaceAll("\\]$", "");
                	}
                }*/
                results.add(result);
            }
    	} else if(selector instanceof NodeSelector) {
    		BaseNodeSelector selectorTemp = (BaseNodeSelector)selector;
    		for (String string : strings) {
                String result = selectorTemp.select(string);
                if(result == null) {
                	continue;
                }
                results.add(result);
            }
    	} else if(selector instanceof BaseElementSelector) {
    		BaseElementSelector selectorTemp = (BaseElementSelector)selector;
    		for (String string : strings) {
                String result = selectorTemp.select(string);
                if(result == null) {
                	continue;
                }
                results.add(result);
            }
    	}
        return new PlainText(results);
    }

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: select
	 * @Description: 根据指定的选择器对多段文本进行提取
	 * @param @param selector
	 * @param @param strings
	 * @param @return
	 * @return Selectable
	 * @throws
	 */
    protected Selectable selectList(Selector selector, List<String> strings) {
    	List<String> results = new ArrayList<String>();
    	if(selector instanceof PlainTextSelector) {
    		PlainTextSelector selectorTemp = (PlainTextSelector)selector;
            for (String string : strings) {
                List<String> result = selectorTemp.selectList(string);
                if(result == null || result.size() <= 0) {
                	continue;
                }
                results.addAll(result);
            }
    	} else if(selector instanceof BaseNodeSelector) {
    		BaseNodeSelector selectorTemp = (BaseNodeSelector)selector;
    		for (String string : strings) {
                List<String> result = selectorTemp.selectList(string);
                if(result == null || result.size() <= 0) {
                	continue;
                }
                results.addAll(result);
            }
    	} else if(selector instanceof BaseElementSelector) {
    		BaseElementSelector selectorTemp = (BaseElementSelector)selector;
    		for (String string : strings) {
                List<String> result = selectorTemp.selectList(string);
                if(result == null || result.size() <= 0) {
                	continue;
                }
                results.addAll(result);
            }
    	}
        return new PlainText(results);
        
    }
    
    public String getFirstSourceText() {
        if (getSourceTexts() != null && getSourceTexts().size() > 0) {
            return getSourceTexts().get(0);
        }
        return null;
    }
}
