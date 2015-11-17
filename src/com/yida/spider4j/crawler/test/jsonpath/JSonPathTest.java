package com.yida.spider4j.crawler.test.jsonpath;

import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.yida.spider4j.crawler.selector.jsonpath.JsonPathUtils;
/**
 * @ClassName: JSonPathTest
 * @Description: JSonPath使用测试
 *               依赖4个jar包:
 *                            json-path-version.jar
 *                            json-smart-version.jar
 *                            slf4j-api-version.jar
 *                            asm-version.jar
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月26日 下午1:04:38
 *
 */
@SuppressWarnings("unused")
public class JSonPathTest {
	public static void main(String[] args) {
		String jsonString = "{\"store\": {" + 
		    "\"book\": [" + 
		      "{" +
		        "\"author\": \"Nigel Rees\"," +
		        "\"title\": [\"Sayings of the Century\",\"Days goes by\"]," +
		        "\"price\": 8.95" +
		      "}," +
		      "{\"category\": \"shoes X\"," +
		        "\"author\": \"Keith erben\"," +
		        "\"title\": [\"put your hands up\",\"the day brand new\"]," +
		        "\"price\": 123.09" +
		      "}," +
		      "{\"category\": \"fiction\"," +
		        "\"author\": \"Evelyn Waugh\"," +
		        "\"title\": \"Sword of Honour\"," +
		        "\"price\": 12.99," +
		        "\"isbn\": \"0-553-21311-3\"" +
		      "}" +
		    "]," +
		    "\"bicycle\": {" +
		      "\"color\": \"red\"," +
		      "\"price\": 19.95" +
		    "}" +
		  "},\"obj\":[{\"a\":\"aaaa\"},{\"b\":\"bbbb\"}]" +
		"}";
		
		List<Map<String,Object>> maps = JsonPathUtils.selectMultiMap(jsonString, "$.store.book[?(@.category)]");
		//printMapList(maps);
		
		
		Map<String,Object> map = JsonPathUtils.selectMap(jsonString, "$.store.book[?(@.category == 'reference')]");
		//printMap2(JsonPathUtils.convertMap(map,null));

		List<String> strList = JsonPathUtils.selectList(jsonString, "$.obj");
		printList(strList);
	}
	
	private static void printMapList(List<Map<String,Object>> maps) {
		if(maps == null || maps.size() <= 0) {
			return;
		}
		for(Map<String,Object> map : maps) {
			printMap(map);
		}
	}

	private static void printMap2(Map<String,String> map) {
		if(map == null || map.isEmpty()) {
			return;
		}
		for(Map.Entry<String,String> entry : map.entrySet()) {
			String key = entry.getKey();
			String val = entry.getValue();
			System.out.println(key + "-->" + val);
		}
	}
	
	private static void printMap(Map<String,Object> map) {
		if(map == null || map.isEmpty()) {
			return;
		}
		for(Map.Entry<String,Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object val = entry.getValue();
			System.out.println(key + "-->" + val);
		}
	}
	
	private static void printList(List<String> authors) {
		if(authors == null || authors.size() <= 0) {
			System.out.println("Nothing.");
			return;
		}
		for(String author: authors) {
			System.out.println(author);
		}
	}
}
