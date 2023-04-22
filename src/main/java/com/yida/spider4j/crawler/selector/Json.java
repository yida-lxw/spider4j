package com.yida.spider4j.crawler.selector;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.yida.spider4j.crawler.selector.jsonpath.JsonPathSelector;

/**
 * @ClassName: Json
 * @Description: Json
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月9日 上午9:42:33
 *
 */
public class Json extends PlainText {

    public Json(List<String> strings) {
        super(strings);
    }

    public Json(String text) {
        super(text);
    }

    /**
     * remove padding for JSONP
     * @param padding
     * @return
     */
    public Json removePadding(String padding) {
        String text = getFirstSourceText();
        XTokenQueue tokenQueue = new XTokenQueue(text);
        tokenQueue.consumeWhitespace();
        tokenQueue.consume(padding);
        tokenQueue.consumeWhitespace();
        String chompBalanced = tokenQueue.chompBalancedNotInQuotes('(', ')');
        return new Json(chompBalanced);
    }

    public <T> T toObject(Class<T> clazz) {
        if (getFirstSourceText() == null) {
            return null;
        }
        return JSON.parseObject(getFirstSourceText(), clazz);
    }

    public <T> List<T> toList(Class<T> clazz) {
        if (getFirstSourceText() == null) {
            return null;
        }
        return JSON.parseArray(getFirstSourceText(), clazz);
    }

    @Override
    public Selectable jsonPath(String jsonPath, boolean multi) {
    	JsonPathSelector jsonPathSelector = new JsonPathSelector(jsonPath);
    	if(multi) {
    		return selectList(jsonPathSelector,getSourceTexts());
    	}
        return select(jsonPathSelector,getSourceTexts());
    }
    
    @Override
    public Selectable jsonPath(String jsonPath) {
    	return jsonPath(jsonPath,false);
    }
}
