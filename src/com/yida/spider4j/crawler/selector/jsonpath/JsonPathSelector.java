package com.yida.spider4j.crawler.selector.jsonpath;

import java.util.List;

import com.yida.spider4j.crawler.selector.PlainTextSelector;
/**
 * @ClassName: JsonPathSelector
 * @Description: JsonPath选择器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月26日 下午9:31:05
 *
 */
public class JsonPathSelector implements PlainTextSelector {
	/**JsonPath表达式*/
	private String jsonPathExpression;
	
	public JsonPathSelector(String jsonPathExpression) {
		this.jsonPathExpression = jsonPathExpression;
	}

	@Override
	public String select(String text) {
		return JsonPathUtils.selectOne(text, jsonPathExpression);
	}

	@Override
	public List<String> selectList(String text) {
		return JsonPathUtils.selectList(text, jsonPathExpression);
	}

	public String getJsonPathExpression() {
		return jsonPathExpression;
	}

	public void setJsonPathExpression(String jsonPathExpression) {
		this.jsonPathExpression = jsonPathExpression;
	}
}
