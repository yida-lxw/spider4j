package com.yida.spider4j.crawler.selector;

import java.util.ArrayList;
import java.util.List;

import com.yida.spider4j.crawler.selector.jsoup.BaseElementSelector;
import com.yida.spider4j.crawler.selector.xpath.BaseNodeSelector;

/**
 * @ClassName: AndSelector
 * @Description: And选择器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月8日 下午4:45:59
 *
 */
public class AndSelector implements PlainTextSelector {
	private List<Selector> selectors = new ArrayList<Selector>();
	
	public AndSelector(Selector[] selectors) {
		if(null == selectors || selectors.length == 0) {
			return;
		}
        for (Selector selector : selectors) {
            this.selectors.add(selector);
        }
    }

    public AndSelector(List<Selector> selectors) {
        this.selectors = selectors;
    }
	
	@Override
	public String select(String text) {
		if (text == null) {
            return null;
        }
        for (Selector selector : selectors) {
        	if(selector instanceof PlainTextSelector) {
        		PlainTextSelector plainTextSelector = (PlainTextSelector)selector;
        		text = plainTextSelector.select(text);
        	} else if(selector instanceof BaseNodeSelector) {
        		BaseNodeSelector baseNodeSelector = (BaseNodeSelector)selector;
        		text = baseNodeSelector.select(text);
        	} else if(selector instanceof BaseElementSelector) {
        		BaseElementSelector baseElementSelector = (BaseElementSelector)selector;
        		text = baseElementSelector.select(text);
        	} else {
        		continue;
        	}
        }
        return text;
	}

	@Override
	public List<String> selectList(String text) {
		if (text == null) {
            return null;
        }
        List<String> results = new ArrayList<String>();
        boolean first = true;
        for (Selector selector : selectors) {
        	if(selector instanceof PlainTextSelector) {
        		PlainTextSelector selectorTemp = (PlainTextSelector)selector;
        		if (first) {
                    results = selectorTemp.selectList(text);
                    first = false;
                } else {
                    List<String> resultsTemp = new ArrayList<String>();
                    for (String result : results) {
                        resultsTemp.addAll(selectorTemp.selectList(result));
                    }
                    results = resultsTemp;
                    if (results == null || results.size() == 0) {
                        return results;
                    }
                }
        	} else if(selector instanceof BaseNodeSelector) {
        		BaseNodeSelector selectorTemp = (BaseNodeSelector)selector;
        		if (first) {
                    results = selectorTemp.selectList(text);
                    first = false;
                } else {
                    List<String> resultsTemp = new ArrayList<String>();
                    for (String result : results) {
                        resultsTemp.addAll(selectorTemp.selectList(result));
                    }
                    results = resultsTemp;
                    if (results == null || results.size() == 0) {
                        return results;
                    }
                }
        	} else if(selector instanceof BaseElementSelector) {
        		BaseElementSelector selectorTemp = (BaseElementSelector)selector;
        		if (first) {
                    results = selectorTemp.selectList(text);
                    first = false;
                } else {
                    List<String> resultsTemp = new ArrayList<String>();
                    for (String result : results) {
                        resultsTemp.addAll(selectorTemp.selectList(result));
                    }
                    results = resultsTemp;
                    if (results == null || results.size() == 0) {
                        return results;
                    }
                }
        	} else {
        		continue;
        	}
        }
        return results;
	}
}
