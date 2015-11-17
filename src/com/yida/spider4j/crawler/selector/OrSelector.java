package com.yida.spider4j.crawler.selector;

import java.util.ArrayList;
import java.util.List;

import com.yida.spider4j.crawler.selector.jsoup.BaseElementSelector;
import com.yida.spider4j.crawler.selector.xpath.BaseNodeSelector;

public class OrSelector implements PlainTextSelector {
	private List<Selector> selectors = new ArrayList<Selector>();

    public OrSelector(Selector[] selectors) {
    	if(null == selectors || selectors.length == 0) {
			return;
		}
        for (Selector selector : selectors) {
            this.selectors.add(selector);
        }
    }

    public OrSelector(List<Selector> selectors) {
        this.selectors = selectors;
    }

    @Override
    public String select(String text) {
    	if (text == null) {
            return null;
        }
        for (Selector selector : selectors) {
        	if(selector instanceof PlainTextSelector) {
        		PlainTextSelector selectorTemp = (PlainTextSelector)selector;
        		String result = selectorTemp.select(text);
                if (result != null && !"".equals(result)) {
                    return result;
                }
        	} else if(selector instanceof BaseNodeSelector) {
        		BaseNodeSelector selectorTemp = (BaseNodeSelector)selector;
        		String result = selectorTemp.select(text);
                if (result != null && !"".equals(result)) {
                    return result;
                }
        	} else if(selector instanceof BaseElementSelector) {
        		BaseElementSelector selectorTemp = (BaseElementSelector)selector;
        		String result = selectorTemp.select(text);
                if (result != null && !"".equals(result)) {
                    return result;
                }
        	} else {
        		continue;
        	}
        }
        return null;
    }

    @Override
    public List<String> selectList(String text) {
    	if (text == null) {
            return null;
        }
        List<String> results = new ArrayList<String>();
        for (Selector selector : selectors) {
        	if(selector instanceof PlainTextSelector) {
        		PlainTextSelector selectorTemp = (PlainTextSelector)selector;
        		List<String> strings = selectorTemp.selectList(text);
        		if(strings == null || strings.size() == 0) {
        			continue;
        		}
                results.addAll(strings);
        	} else if(selector instanceof BaseNodeSelector) {
        		BaseNodeSelector selectorTemp = (BaseNodeSelector)selector;
        		List<String> strings = selectorTemp.selectList(text);
        		if(strings == null || strings.size() == 0) {
        			continue;
        		}
                results.addAll(strings);
        	} else if(selector instanceof BaseElementSelector) {
        		BaseElementSelector selectorTemp = (BaseElementSelector)selector;
        		List<String> strings = selectorTemp.selectList(text);
        		if(strings == null || strings.size() == 0) {
        			continue;
        		}
                results.addAll(strings);
        	} else {
        		continue;
        	}
        }
        return results;
    }
}
